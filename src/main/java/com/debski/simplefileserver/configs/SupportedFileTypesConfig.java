package com.debski.simplefileserver.configs;

import com.debski.simplefileserver.exceptions.UnsupportedFileTypeException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Configuration
@Slf4j
public class SupportedFileTypesConfig {

    @Bean
    public Set<String> supportedFileTypes(@NonNull @Value("${supported-file-types}") Set<String> supportedFileTypes) {
        supportedFileTypes = lowerCaseAllItems(supportedFileTypes);
        log.info("Supported file types are: {}", supportedFileTypes);
        return supportedFileTypes;
    }

    @Bean
    public Map<String, String> supportedContentTypesProvider(Set<String> supportedFileTypes) {
        Map<String, String> supportedContentTypesProvider =
                supportedFileTypes.stream()
                        .collect(toMap(k -> k, this::mapToContentTypeHeaderValue));
        log.info("Supported content types are: {}", supportedContentTypesProvider);
        return supportedContentTypesProvider;
    }

    private Set<String> lowerCaseAllItems(Set<String> supportedFileTypes) {
        return supportedFileTypes.stream().map(StringUtils::lowerCase).collect(Collectors.toSet());
    }

    private String mapToContentTypeHeaderValue(String supportedType) {
        //TODO implement additional mappings or find lib which will provide below mapping logic instead of implementing it
        switch (supportedType.toLowerCase()) {
            case "txt":
                return "text/plain";
            case "json":
                return "application/json";
            case "csv":
                return "text/csv";
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "gif":
                return "image/gif";
            case "css":
                return "text/css";
            case "js":
                return "text/javascript";
            case "pdf":
                return "application/pdf";
            default:
                throw new UnsupportedFileTypeException(
                        String.format("Cannot create supportedContentTypesProvider because of missing mapping-to-content-type logic for supported type: '%s'", supportedType));
        }
    }
}
