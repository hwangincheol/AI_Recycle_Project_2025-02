package com.lrin.project.repository.member;

import com.lrin.project.entity.member.MemberEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    //아이디 중복 확인
    @Query(value = "select id from member where id = :id", nativeQuery = true)
    String findMember(@Param("id") String id);

    //시큐리티
    MemberEntity findOneById(String name);

    @Query(value = "select pw from member where id = :id", nativeQuery = true)
    String idPwChk(@Param("id") String id);

    @Query(value = "select id, pw, name, addr, streetaddr, detailaddr, tel, reg_time, update_time, created_by, modified_by, role from member where id = :id", nativeQuery = true)
    MemberEntity memberInfo(@Param("id") String id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE member SET " +
            "   addr = :addr, " +
            "   streetaddr = :streetaddr, " +
            "   detailaddr = :detailaddr, " +
            "   tel = :tel, " +
            "   modified_by = :id " +
            "WHERE id = :id", nativeQuery = true)
    void myUpdate(
            @Param("addr") String addr,
            @Param("streetaddr") String streetaddr,
            @Param("detailaddr") String detailaddr,
            @Param("tel") String tel,
            @Param("id") String id);

    @Query(value = "select id from member where " +
            "   name = :name and " +
            "   tel = :tel", nativeQuery = true)
    String idFind(
            @Param("name") String name,
            @Param("tel") String tel);

    @Transactional
    @Query(value = "select id from member where " +
            "   id = :id and " +
            "   name = :name and " +
            "   tel = :tel", nativeQuery = true)
    String pwFind(
            @Param("id") String id,
            @Param("name") String name,
            @Param("tel") String tel);

    @Transactional
    @Modifying
    @Query(value = "update member set pw = :pw " +
            "where id = :id", nativeQuery = true)
    void pwSetting(@Param("id") String id,
                   @Param("pw") String pw);
}