package com.debski.simplefileserver.exceptions.handlers;

import com.debski.simplefileserver.exceptions.FileNotFoundException;
import com.debski.simplefileserver.exceptions.IllegalFileTypeChangeException;
import com.debski.simplefileserver.exceptions.UnsupportedFileTypeException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Hidden
@Slf4j
public class RuntimeExceptionsHandler {

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ExceptionHandler(value = {UnsupportedFileTypeException.class})
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public String handleUnsupportedFileTypeException(UnsupportedFileTypeException ex) {
        log.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ExceptionHandler(value = {FileNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleFileNotFoundException(FileNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ExceptionHandler(value = {IllegalFileTypeChangeException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public String handleIllegalFileTypeChangeException(IllegalFileTypeChangeException ex) {
        log.error(ex.getMessage(), ex);
        return ex.getMessage();
    }
}