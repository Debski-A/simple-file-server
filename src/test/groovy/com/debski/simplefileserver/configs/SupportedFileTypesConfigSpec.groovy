package com.debski.simplefileserver.configs

import com.debski.simplefileserver.exceptions.UnsupportedFileTypeException
import spock.lang.Specification

import java.util.stream.Collectors
import java.util.stream.Stream

class SupportedFileTypesConfigSpec extends Specification {

    def 'should throw exception during supportedContentTypesProvider creation if content-type resolving logic is missing for particular supported file type'() {
        given: "supported file type 'abcd' content-type resolving logic is not implemented"
        Set<String> supportedFileTypes = Stream.of('txt', 'json', 'csv', 'abcd').collect(Collectors.toSet())

        when:
        new SupportedFileTypesConfig().supportedContentTypesProvider(supportedFileTypes)

        then: 'informative error is thrown during creation'
        def exception = thrown(UnsupportedFileTypeException)
        exception.message == "Cannot create supportedContentTypesProvider because of missing mapping-to-content-type logic for supported type: 'abcd'"
    }

}
