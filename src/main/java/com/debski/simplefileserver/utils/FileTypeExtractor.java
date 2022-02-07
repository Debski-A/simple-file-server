package com.debski.simplefileserver.utils;

import org.apache.commons.io.FilenameUtils;

import static org.apache.commons.lang3.StringUtils.lowerCase;

public class FileTypeExtractor {

    public static String extractFileTypeAndLowerCaseIt(String filename) {
        return lowerCase(FilenameUtils.getExtension(filename));
    }
}
