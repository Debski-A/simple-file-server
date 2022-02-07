package com.debski.simplefileserver.controllers;

import com.debski.simplefileserver.models.FileMetadataModel;
import com.debski.simplefileserver.openapi.FileIdParameter;
import com.debski.simplefileserver.openapi.FileNotFoundApiResponse;
import com.debski.simplefileserver.services.FileManager;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@FileNotFoundApiResponse
@Slf4j
@RestController
@RequestMapping(value = "/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class FileMetadataController {

    private final FileManager fileManager;

    @ApiResponse(responseCode = "200", description = "Got whole file metadata for specified file id",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FileMetadataModel.class))
    )
    @GetMapping("/whole/{fileId}")
    public FileMetadataModel getCompleteMetadata(@FileIdParameter @PathVariable Long fileId) {
        return fileManager.getFileMetadata(fileId);
    }


    @ApiResponse(responseCode = "200", description = "Got file original name metadata for specified file id",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\n\t\"originalName\": \"string\"\n}"))
    )
    @GetMapping("/name/{fileId}")
    public FileMetadataModel getOriginalName(@FileIdParameter @PathVariable Long fileId) {
        return fileManager.getFileMetadata(fileId).nullifyOthersThanOriginalName();
    }


    @ApiResponse(responseCode = "200", description = "Got file type metadata for specified file id",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\n\t\"type\": \"string\"\n}"))
    )
    @GetMapping("/type/{fileId}")
    public FileMetadataModel getFileType(@FileIdParameter @PathVariable Long fileId) {
        return fileManager.getFileMetadata(fileId).nullifyOthersThanType();
    }


    @ApiResponse(responseCode = "200", description = "Got file size metadata for specified file id",
            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\n\t\"size\": 0\n}"))
    )
    @GetMapping("/size/{fileId}")
    public FileMetadataModel getSize(@FileIdParameter @PathVariable Long fileId) {
        return fileManager.getFileMetadata(fileId).nullifyOthersThanSize();
    }
}
