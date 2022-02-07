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
@Table(name = "file_content")
public class FileContent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Lob
    @NotNull
    @Setter
    private byte[] content;

    public FileContent(byte[] content) {
        this.content = content;
    }
}
