package com.debski.simplefileserver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
class SimpleFileServerApplicationIntegrationSpec extends Specification {

    @Autowired
    WebApplicationContext webAppContext

    MockMvc mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build()
    }

    def 'test file not found scenario'() {
        expect:
        mockMvc.perform(get('/1'))
                .andExpect(status().isNotFound())
    }

    def 'test unsupported file type scenario'() {
        given:
        MultipartFile fileWithUnsupportedType =
                new MockMultipartFile(
                        'newFile',
                        'some file.xyz',
                        MediaType.TEXT_PLAIN_VALUE,
                        'some text'.getBytes())

        expect:
        mockMvc.perform(multipart('/create').file(fileWithUnsupportedType))
                .andExpect(status().isUnsupportedMediaType())
    }

    def 'test illegal file type change scenario'() {
        given:
        MultipartFile originalFile =
                new MockMultipartFile(
                        'newFile',
                        'some file.txt',
                        MediaType.TEXT_PLAIN_VALUE,
                        'some text'.getBytes())
        MultipartFile newFileWithDifferentType =
                new MockMultipartFile(
                        'newFile',
                        'some file.json',
                        MediaType.TEXT_PLAIN_VALUE,
                        'some text'.getBytes())

        when: 'file is persisted and generated id is returned'
        var id = mockMvc.perform(multipart('/create').file(originalFile))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()

        then: 'updating original file with new file of different type triggers exception'
        mockMvc.perform(multipartPatch("/update/${id}").file(newFileWithDifferentType))
                .andExpect(status().isUnprocessableEntity())
    }

    def 'test successful update scenario'() {
        given:
        MultipartFile originalFile =
                new MockMultipartFile(
                        'newFile',
                        'some file.txt',
                        MediaType.TEXT_PLAIN_VALUE,
                        'some text'.getBytes())
        MultipartFile newFile =
                new MockMultipartFile(
                        'newFile',
                        'another file.txt',
                        MediaType.TEXT_PLAIN_VALUE,
                        'some new content'.getBytes())

        when: 'file is persisted and generated id is returned'
        var id = mockMvc.perform(multipart('/create').file(originalFile))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()

        then: 'updating original file with new file of the same type is successful'
        mockMvc.perform(multipartPatch("/update/${id}").file(newFile))
                .andExpect(status().isOk())
    }

    // there is no multipart patch request method provided directly from Spring libs
    private MockMultipartHttpServletRequestBuilder multipartPatch(String uri) {
        MockMultipartHttpServletRequestBuilder builder =
                multipart(uri)
        builder.with(new RequestPostProcessor() {
            @Override
            MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH")
                request
            }
        })
        builder
    }

    def 'test successful get and delete scenario'() {
        given:
        MultipartFile file =
                new MockMultipartFile(
                        'newFile',
                        'some file.json',
                        MediaType.TEXT_PLAIN_VALUE,
                        'some text'.getBytes())

        when: 'file is persisted and generated id is returned'
        var id = mockMvc.perform(multipart('/create').file(file))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()

        then: 'particular file can be requested by id'
        mockMvc.perform(get("/get/${id}"))
                .andExpect(status().isOk())
                .andExpect(content().string('some text'))

        and: 'metadata for particular file can be requested by id'
        mockMvc.perform(get("/metadata/name/${id}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string('{"originalName":"some file"}'))

        and: 'particular file can be deleted by id'
        mockMvc.perform(delete("/delete/${id}"))
                .andExpect(status().isOk())

        and: 'particular file cannot be requested anymore after successful deletion'
        mockMvc.perform(get("/get/${id}"))
                .andExpect(status().isNotFound())
    }
}
