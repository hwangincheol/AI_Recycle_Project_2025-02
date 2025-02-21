package com.lrin.project.entity.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity  // 클래스를 엔티티로 선언
@Table(name="item") // 엔티티와 매핑할 테이블을 지정
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id  // 기본키에 사용할 속성 지정 가능
    @Column(name="item_name")
    private String itemName; //상품명

    @Column(name="item_name_kor")
    private String itemNameKor; // 편의상 한글 이름용

    @Column(nullable = false)
    private int price; //가격





}
