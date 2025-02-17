package com.lrin.project.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}
