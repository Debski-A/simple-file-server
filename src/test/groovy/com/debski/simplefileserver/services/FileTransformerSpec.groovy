package com.debski.simplefileserver.services

import com.debski.simplefileserver.entities.File
import com.debski.simplefileserver.entities.FileContent
import com.debski.simplefileserver.models.FileMetadataModel
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification

class FileTransformerSpec extends Specification {

    FileTransformer transformer = new FileTransformer()

    def 'should map to file entity'() {
        given:
        MultipartFile multipartFile = provideMultipartFileStub()

        when:
        File fileEntity = transformer.mapToFileEntity(multipartFile)

        then: 'ids are not set since entity is in transient state'
        fileEntity.getId() == null

        and: 'other values are set accordingly'
        fileEntity.getFileContent().getContent() == multipartFile.getBytes()
        fileEntity.getOriginalName() == 'some_file'
        fileEntity.getType() == 'txt'
        fileEntity.getSize() == 33L
    }

    def 'should update file entity'() {
        given: 'already persisted entity'
        byte[] content = [1, 1, 1] as byte[]
        File managedFileEntity = new File(new FileContent(content), 44L, "original_file_name", "txt")

        and: 'multipart file stub'
        MultipartFile multipartFile = provideMultipartFileStub()

        when:
        transformer.updateFileEntity(managedFileEntity, multipartFile)

        then: 'original name did not change'
        managedFileEntity.getOriginalName() == 'original_file_name'

        and: 'other values are updated accordingly'
        managedFileEntity.getFileContent().getContent() == multipartFile.getBytes()
        managedFileEntity.getType() == 'txt'
        managedFileEntity.getSize() == 33L
    }

    def 'should map metadata to metadata model'() {
        given:
        byte[] content = [1, 1, 1] as byte[]
        File managedFileEntity = new File(new FileContent(content), 44L, "original_file_name", "txt")

        when:
        FileMetadataModel metadataDto = transformer.mapToMetadataModel(managedFileEntity)

        then:
        metadataDto.getOriginalName() == 'original_file_name'
        metadataDto.getType() == 'txt'
        metadataDto.getSize() == 44L
    }

    private MultipartFile provideMultipartFileStub() {
        byte[] stubbedContent = [0, 0, 0] as byte[]
        MultipartFile multipartFile = Stub()
        multipartFile.getBytes() >> stubbedContent
        multipartFile.getOriginalFilename() >> 'some_file.txt'
        multipartFile.getSize() >> 33L
        multipartFile
    }
}
