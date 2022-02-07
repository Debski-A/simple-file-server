package com.debski.simplefileserver.models


import spock.lang.Specification

class FileMetadataModelSpec extends Specification {

    FileMetadataModel model = FileMetadataModel.builder()
            .size(44L)
            .originalName('some file.txt')
            .type('txt')
            .build()

    def 'should nullify others than original name field'() {
        when:
        model = model.nullifyOthersThanOriginalName()

        then:
        model.getOriginalName() == 'some file.txt'
        model.getSize() == null
        model.getType() == null
    }

    def 'should nullify others than type field'() {
        when:
        model = model.nullifyOthersThanType()

        then:
        model.getOriginalName() == null
        model.getSize() == null
        model.getType() == 'txt'
    }

    def 'should nullify others than size field'() {
        when:
        model = model.nullifyOthersThanSize()

        then:
        model.getOriginalName() == null
        model.getSize() == 44L
        model.getType() == null
    }
}
