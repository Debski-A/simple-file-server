package com.debski.simplefileserver.services;

import com.debski.simplefileserver.entities.File;
import com.debski.simplefileserver.entities.FileContent;
import com.debski.simplefileserver.models.FileMetadataModel;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.debski.simplefileserver.utils.FileTypeExtractor.extractFileTypeAndLowerCaseIt;
import static org.apache.commons.io.FilenameUtils.getBaseName;

@Component
class FileTransformer {

    File mapToFileEntity(@NonNull MultipartFile multipartFile) {
        try {
            return new File(
                    new FileContent(multipartFile.getBytes()),
                    multipartFile.getSize(),
                    getBaseName(multipartFile.getOriginalFilename()),
                    extractFileTypeAndLowerCaseIt(multipartFile.getOriginalFilename()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    void updateFileEntity(@NonNull File file, @NonNull MultipartFile multipartFile) {
        try {
            file.setType(extractFileTypeAndLowerCaseIt(multipartFile.getOriginalFilename()));
            file.setSize(multipartFile.getSize());
            file.getFileContent().setContent(multipartFile.getBytes());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    FileMetadataModel mapToMetadataModel(@NonNull File fileEntity) {
        return FileMetadataModel.builder()
                .originalName(fileEntity.getOriginalName())
                .type(fileEntity.getType())
                .size(fileEntity.getSize())
                .build();
    }
}
