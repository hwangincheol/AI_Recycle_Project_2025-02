package com.lrin.project.repository.boardfile;

import com.lrin.project.entity.boardfile.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    // CRUD 처리
}
