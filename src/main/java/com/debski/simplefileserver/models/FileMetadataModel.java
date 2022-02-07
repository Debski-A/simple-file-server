package com.debski.simplefileserver.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@ToString
public class FileMetadataModel {

    private Long size;

    private String originalName;

    private String type;

    public FileMetadataModel nullifyOthersThanSize() {
        this.originalName = null;
        this.type = null;
        return this;
    }

    public FileMetadataModel nullifyOthersThanOriginalName() {
        this.size = null;
        this.type = null;
        return this;
    }

    public FileMetadataModel nullifyOthersThanType() {
        this.size = null;
        this.originalName = null;
        return this;
    }
}
