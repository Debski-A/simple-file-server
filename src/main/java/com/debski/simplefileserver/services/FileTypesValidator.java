package com.debski.simplefileserver.services;

import com.debski.simplefileserver.exceptions.IllegalFileTypeChangeException;
import com.debski.simplefileserver.exceptions.UnsupportedFileTypeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.debski.simplefileserver.utils.FileTypeExtractor.extractFileTypeAndLowerCaseIt;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

@Component
@Slf4j
class FileTypesValidator {

    private final Set<String> supportedFileTypes;

    FileTypesValidator(Set<String> supportedFileTypes) {
        this.supportedFileTypes = supportedFileTypes;
    }

    void checkIfFileTypeIsSupported(String filename) {
        String fileType = extractFileTypeAndLowerCaseIt(filename);
        supportedFileTypes.stream()
                .filter(supportedFileType -> equalsIgnoreCase(supportedFileType, fileType))
                .findAny()
                .orElseThrow(() -> new UnsupportedFileTypeException(String.format("File type from filename: '%s' not supported", filename)));
    }

    void checkIfNewFileTypeIsSameAsOriginalOne(String newFilename, String originalFileType) {
        String newFileType = extractFileTypeAndLowerCaseIt(newFilename);
        if (!StringUtils.equalsIgnoreCase(newFileType, originalFileType)) {
            throw new IllegalFileTypeChangeException(String.format("File type from new filename: '%s' is different than original file type", newFilename));
        }
    }
}
