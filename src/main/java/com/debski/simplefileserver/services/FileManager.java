package com.debski.simplefileserver.services;

import com.debski.simplefileserver.entities.File;
import com.debski.simplefileserver.exceptions.FileNotFoundException;
import com.debski.simplefileserver.exceptions.IllegalFileTypeChangeException;
import com.debski.simplefileserver.exceptions.UnsupportedFileTypeException;
import com.debski.simplefileserver.models.FileMetadataModel;
import com.debski.simplefileserver.repositories.FileRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileManager {

    private final FileRepository fileRepository;
    private final FileTypesValidator validator;
    private final FileTransformer transformer;
    private final ResponseHeadersResolver headersResolver;

    /**
     * Transactional method.
     * Validates file, persists it in database and returns auto-generated id
     *
     * @param newFile file received from upstream
     * @return id of persisted file
     * @throws UnsupportedFileTypeException type of newFile is not supported
     */
    @Transactional
    public Long persistAndReturnId(@NonNull MultipartFile newFile) {
        //TODO it would be nice to have max file size constraint
        validator.checkIfFileTypeIsSupported(newFile.getOriginalFilename());
        final File transientFileEntity = transformer.mapToFileEntity(newFile);
        final File managedFileEntity = fileRepository.save(transientFileEntity);
        log.info("Assigned id: {} to file: {}", managedFileEntity.getId(), managedFileEntity.getOriginalName());
        return managedFileEntity.getId();
    }

    /**
     * Transactional method.
     * Validates new file and uses its content to update existing file
     *
     * @param id      id of original file
     * @param newFile file received from upstream
     * @throws UnsupportedFileTypeException   type of newFile is not supported
     * @throws FileNotFoundException          original file for particular id is not found
     * @throws IllegalFileTypeChangeException newFile type is different from original file type
     */
    @Transactional
    public void update(@NonNull Long id, @NonNull MultipartFile newFile) {
        //TODO it would be nice to have max file size constraint
        validator.checkIfFileTypeIsSupported(newFile.getOriginalFilename());
        final File managedFileEntity = verifyIfFileExistsAndFetchIt(id);
        validator.checkIfNewFileTypeIsSameAsOriginalOne(newFile.getOriginalFilename(), managedFileEntity.getType());
        transformer.updateFileEntity(managedFileEntity, newFile);
    }

    /**
     * Transactional method.
     * Gets file from database and prepares http response entity
     *
     * @param id id of original file
     * @return http response entity
     * @throws FileNotFoundException original file for particular id is not found
     */
    @Transactional
    public ResponseEntity<Resource> getFileAndProvideResponse(@NonNull Long id) {
        final File managedFileEntity = verifyIfFileExistsAndFetchIt(id);
        log.info("Found file: {} for specified id: {}", managedFileEntity.getOriginalName(), managedFileEntity.getId());
        final FileMetadataModel model = transformer.mapToMetadataModel(managedFileEntity);
        final HttpHeaders headers = headersResolver.resolveHeaders(model);
        return new ResponseEntity<>(new ByteArrayResource(managedFileEntity.getFileContent().getContent()), headers, HttpStatus.OK);
    }

    /**
     * Transactional method.
     * Deletes file identified by provided id
     *
     * @param id id of original file
     * @throws FileNotFoundException original file for particular id is not found
     */
    @Transactional
    public void deleteFile(@NonNull Long id) {
        if (fileRepository.existsById(id)) {
            fileRepository.deleteById(id);
        } else {
            throw new FileNotFoundException(String.format("File identified by id = %s not found", id));
        }
    }

    /**
     * Transactional method.
     * Gets file metadata from database and prepares model
     *
     * @param id id of original file
     * @return file metadata model
     * @throws FileNotFoundException original file for particular id is not found
     */
    @Transactional
    public FileMetadataModel getFileMetadata(@NonNull Long id) {
        final File managedFileEntity = verifyIfFileExistsAndFetchIt(id);
        log.info("Found file metadata related to file: {} for specified id: {}", managedFileEntity.getOriginalName(), managedFileEntity.getId());
        return transformer.mapToMetadataModel(managedFileEntity);
    }

    private File verifyIfFileExistsAndFetchIt(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException(String.format("File identified by id = %s not found", id)));
    }
}
