package com.lrin.project.service.member;

import com.lrin.project.entity.member.MemberEntity;
import com.lrin.project.repository.member.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    public final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String name) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        MemberEntity memberEntity = memberRepository.findOneById(name);
        if(Objects.equals(memberEntity.getId(), "admin") && memberEntity.getRole().equals("admin")){
            grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
            return new User(memberEntity.getId(), memberEntity.getPw(), grantedAuthorities);
        }
        else if(memberEntity.getId()!=null && !memberEntity.getId().equals("admin")){
            grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
            return new User(memberEntity.getId(), memberEntity.getPw(), grantedAuthorities);
        }
        else{
            throw new UsernameNotFoundException("아이디를 찾을수 없습니다.");
        }
    }
}
