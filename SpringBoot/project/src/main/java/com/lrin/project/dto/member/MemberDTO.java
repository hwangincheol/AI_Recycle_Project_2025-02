package com.lrin.project.dto.member;

import com.lrin.project.entity.member.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberDTO {
    private String id;
    private String pw;
    private String name;
    private String addr;
    private String streetaddr;
    private String detailaddr;
    private String tel;
    private String role;

    public MemberEntity toEntity() {
        return MemberEntity.builder()
            .id(id)
            .pw(pw)
            .name(name)
            .addr(addr)
            .streetaddr(streetaddr)
            .detailaddr(detailaddr)
            .tel(tel)
            .role(role)
            .build();
    }
}