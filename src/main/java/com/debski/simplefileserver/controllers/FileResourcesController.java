package com.debski.simplefileserver.controllers;

import com.debski.simplefileserver.openapi.*;
import com.debski.simplefileserver.services.FileManager;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileResourcesController {

    private final FileManager fileManager;

    @ApiResponse(responseCode = "200", description = "Got file for specified file id",
            content = @Content(mediaType = "refer to supported types")
    )
    @FileNotFoundApiResponse
    @GetMapping("/get/{fileId}")
    public ResponseEntity<Resource> get(@FileIdParameter @PathVariable Long fileId) {
        log.info("About to get file identified by id: {}", fileId);
        return fileManager.getFileAndProvideResponse(fileId);
    }


    @ApiResponse(responseCode = "200", description = "Uploaded file and returned generated id",
            content = @Content(mediaType = "text/plain"))
    @UnsupportedFileTypeApiResponse
    @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String create(@FileBodyParameter @RequestParam MultipartFile newFile) {
        log.info("About to create new file: {}", newFile.getOriginalFilename());
        return fileManager.persistAndReturnId(newFile).toString();
    }


    @UnsupportedFileTypeApiResponse
    @FileNotFoundApiResponse
    @IllegalFileTypeChangeApiResponse
    @SuccessfulApiResponse
    @PatchMapping(path = "/update/{fileId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void update(@FileIdParameter @PathVariable Long fileId,
                       @FileBodyParameter @RequestParam MultipartFile newFile) {
        log.info("About to update file identified by id: {}", fileId);
        fileManager.update(fileId, newFile);
    }


    @FileNotFoundApiResponse
    @SuccessfulApiResponse
    @DeleteMapping("/delete/{fileId}")
    public void delete(@FileIdParameter @PathVariable Long fileId) {
        log.info("About to delete file identified by id: {}", fileId);
        fileManager.deleteFile(fileId);
    }
}
