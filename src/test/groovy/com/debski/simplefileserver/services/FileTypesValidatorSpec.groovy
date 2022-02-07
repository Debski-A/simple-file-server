package com.debski.simplefileserver.services

import com.debski.simplefileserver.exceptions.IllegalFileTypeChangeException
import com.debski.simplefileserver.exceptions.UnsupportedFileTypeException
import spock.lang.Specification

import java.util.stream.Collectors
import java.util.stream.Stream

class FileTypesValidatorSpec extends Specification {

    FileTypesValidator validator =
            new FileTypesValidator(Stream.of('txt', 'json', 'csv').collect(Collectors.toSet()))

    def 'should NOT throw exception when type is supported'() {
        when:
        validator.checkIfFileTypeIsSupported(providedFilename)

        then:
        notThrown(UnsupportedFileTypeException)

        where:
        providedFilename      | _
        'abc.txt'             | _
        'efg.json'            | _
        '.csv'                | _
        'some file.TXT'       | _
        'this^is&legalll.tXt' | _
        'png.jpg.tXt'         | _
    }

    def 'should throw exception when type is not supported'() {
        when:
        validator.checkIfFileTypeIsSupported(providedFileName)

        then:
        def exception = thrown(UnsupportedFileTypeException)
        exception.message == expectedErrorMessage

        where:
        providedFileName  || expectedErrorMessage
        'abc.txt.'        || "File type from filename: 'abc.txt.' not supported"
        'Some file.t.x.t' || "File type from filename: 'Some file.t.x.t' not supported"
        'abc'             || "File type from filename: 'abc' not supported"
        ' '               || "File type from filename: ' ' not supported"
        ''                || "File type from filename: '' not supported"
        null              || "File type from filename: 'null' not supported"
    }

    def 'should NOT throw exception when provided file type is same as original file type'() {
        when:
        validator.checkIfNewFileTypeIsSameAsOriginalOne(proidedFilename, originalFileType)

        then:
        notThrown(IllegalFileTypeChangeException)

        where:
        proidedFilename | originalFileType
        'file.txt'      | 'txt'
        'x x c.txt'     | 'tXt'
    }

    def 'should throw exception when provided file type is different than original file type'() {
        when:
        validator.checkIfNewFileTypeIsSameAsOriginalOne(proidedFilename, originalFileType)

        then:
        def exception = thrown(IllegalFileTypeChangeException)
        exception.message == expectedErrorMessage

        where:
        proidedFilename | originalFileType || expectedErrorMessage
        'hmm.txt'       | 'csv'            || "File type from new filename: 'hmm.txt' is different than original file type"
        ' '             | 'csv'            || "File type from new filename: ' ' is different than original file type"
        ''              | 'csv'            || "File type from new filename: '' is different than original file type"
        null            | 'csv'            || "File type from new filename: 'null' is different than original file type"
    }

}
