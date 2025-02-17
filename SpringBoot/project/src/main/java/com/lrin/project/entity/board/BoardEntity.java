package com.lrin.project.entity.board;

import com.lrin.project.entity.boardfile.FileEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOARD")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "board_seq_generator",
        sequenceName = "board_SEQ",
        allocationSize = 1 // 시퀀스 증가값을 1로 설정
)
public class BoardEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq_generator")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer = "익명";

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private FileEntity fileEntity;

    public String getFileUrl() {
        return fileEntity != null ? "/uploads/" + fileEntity.getFileName() : null;
    }

}
