package com.lrin.project.controller;

import com.lrin.project.entity.member.MemberEntity;
import com.lrin.project.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class FirstRun implements CommandLineRunner {

    @Value("${spring.jpa.hibernate.ddl-auto}")
    String ddlAuto;

    @Autowired
    MemberService memberService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if ("create".equalsIgnoreCase(ddlAuto)) {
            try {
                System.out.println("DDL-AUTO value is 'create'. Running...");

                // 관리자 계정이 이미 존재하는지 확인
                if (memberService.existsById("admin")) {
                    System.out.println("admin ID is aready exists. Not Running...");
                    return;
                }

                //계정 생성
                //관리자
                MemberEntity admin = MemberEntity.builder()
                        .id("admin")
                        .pw("1")
                        .name("관리자")
                        .addr("000000")
                        .streetaddr("도로명 주소")
                        .detailaddr("상세주소")
                        .tel("010-0000-0000")
                        .role("admin")
                        .build();
                memberService.memberSave(admin);
                //일반 유저
                MemberEntity user = MemberEntity.builder()
                        .id("test")
                        .pw("1")
                        .name("테스터")
                        .addr("000000")
                        .streetaddr("도로명 주소")
                        .detailaddr("상세주소")
                        .tel("010-0000-0000")
                        .role("user")
                        .build();
                memberService.memberSave(user);
                System.out.println("Admin ID made successfully.");

            } catch (Exception e) {
                System.err.println("Admin ID exception: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("DDL-AUTO value is not 'create'. Not Running...");
        }
    }
}
