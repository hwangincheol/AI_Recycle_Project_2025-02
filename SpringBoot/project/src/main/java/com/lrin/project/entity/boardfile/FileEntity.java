package com.lrin.project.entity.boardfile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "upload")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    public FileEntity(String fileName, String fileType, String filePath, Long fileSize) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
}
