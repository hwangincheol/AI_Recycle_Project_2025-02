package com.lrin.project.repository.home;

import com.lrin.project.entity.board.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeRepository extends JpaRepository<BoardEntity, Long> {
    @Query(value = "select * from board b ORDER BY b.reg_time DESC", nativeQuery = true)
    List<BoardEntity> findBoardRecent();
}