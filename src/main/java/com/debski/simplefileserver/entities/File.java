package com.debski.simplefileserver.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class File {

    @Id
    private Long id;

    @Setter
    @NotNull
    private Long size;

    @NotNull
    private String originalName;

    @Setter
    @NotNull
    private String type;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "id")
    @MapsId
    @ToString.Exclude
    private FileContent fileContent;

    public File(FileContent fileContent, Long size, String originalName, String type) {
        this.fileContent = fileContent;
        this.size = size;
        this.originalName = originalName;
        this.type = type;
    }
}
