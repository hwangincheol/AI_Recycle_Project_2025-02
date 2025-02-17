package com.lrin.project.service.member;

import com.lrin.project.dto.member.MemberDTO;
import com.lrin.project.entity.member.MemberEntity;
import com.lrin.project.repository.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImple implements MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String findMember(String id) {
        return memberRepository.findMember(id);
    }

    @Override
    public void memberSave(MemberEntity memberEntity) {
        memberEntity.setPw(bCryptPasswordEncoder.encode(memberEntity.getPw()));
        if(memberEntity.getRole().equals("") || memberEntity.getRole().equals(null)){
            memberEntity.setRole("user");
        }
        memberRepository.save(memberEntity);
    }

    @Override
    public String idPwChk(String id) {
        return memberRepository.idPwChk(id);
    }

    @Override
    public MemberEntity memberInfo(String id) {
        return memberRepository.memberInfo(id);
    }

    @Override
    public void mySomeSave(String addr, String streetaddr, String detailaddr, String tel, String id) {
        memberRepository.myUpdate(addr, streetaddr, detailaddr, tel, id);
    }

    @Override
    public String idFind(String name, String tel) {
        return memberRepository.idFind(name,tel);
    }

    @Override
    public String pwFind(String id, String name, String tel) {
        return memberRepository.pwFind(id,name,tel);
    }

    @Override
    public void pwSetting(String id, String pw) {
        pw = bCryptPasswordEncoder.encode(pw);
        memberRepository.pwSetting(id, pw);
    }

    @Override
    public boolean existsById(String admin) {
        return memberRepository.existsById(admin);
    }
}