package com.lrin.project.entity.member;

import com.lrin.project.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity
@NoArgsConstructor
@Data
@Table(name = "member")
public class MemberEntity extends BaseEntity {
    @Id
    @Column
    private String id;

    @Column
    private String pw;

    @Column
    private String name;

    @Column
    private String addr;

    @Column
    private String streetaddr;

    @Column
    private String detailaddr;

    @Column
    private String tel;

    @Column
    private String role;

    @Builder
    public MemberEntity(String id, String pw, String name, String addr, String streetaddr, String detailaddr, String tel, String role) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.addr = addr;
        this.streetaddr = streetaddr;
        this.detailaddr = detailaddr;
        this.tel = tel;
        this.role = role;
    }
}