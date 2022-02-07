package com.debski.simplefileserver.services;

import com.debski.simplefileserver.models.FileMetadataModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Collections.singletonList;

@Component
@Slf4j
public class ResponseHeadersResolver {

    private final Map<String, String> supportedContentTypesProvider;

    ResponseHeadersResolver(Map<String, String> supportedContentTypesProvider) {
        this.supportedContentTypesProvider = supportedContentTypesProvider;
    }

    HttpHeaders resolveHeaders(FileMetadataModel model) {
        final HttpHeaders headers = new HttpHeaders();
        final String contentType = supportedContentTypesProvider.get(model.getType());
        log.info("Resolved file type: {} to content-type: {}", model.getType(), contentType);
        headers.put("Content-Type", singletonList(contentType));
        headers.put("Content-Length", singletonList(model.getSize().toString()));
        headers.put("Accept-Ranges", singletonList("bytes"));
        headers.put("Original-Filename", singletonList(model.getOriginalName()));
        return headers;
    }
}
