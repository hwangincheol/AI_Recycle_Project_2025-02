package com.lrin.project.repository.board;

import com.lrin.project.entity.board.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Page<BoardEntity> findAll(Pageable pageable); // 페이징 처리

}