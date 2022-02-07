package com.debski.simplefileserver.services


import com.debski.simplefileserver.models.FileMetadataModel
import org.springframework.util.MultiValueMap
import spock.lang.Specification

class ResponseHeadersResolverSpec extends Specification {

    ResponseHeadersResolver headersResolver =
            new ResponseHeadersResolver([txt: 'text/plain', json: 'application/json', csv: 'text/csv'])


    def 'should resolve headers accordingly'() {
        given:
        FileMetadataModel model = FileMetadataModel.builder()
                .originalName('some file.json')
                .type('json')
                .size(33L)
                .build()

        when:
        MultiValueMap<String, String> headers = headersResolver.resolveHeaders(model)

        then:
        headers.getFirst('Content-Type') == 'application/json'
        headers.getFirst('Content-Length') == '33'
        headers.getFirst('Accept-Ranges') == 'bytes'
        headers.getFirst('Original-Filename') == 'some file.json'
    }
}
