package com.lrin.project.controller;

import com.lrin.project.dto.member.MemberDTO;
import com.lrin.project.entity.member.MemberEntity;
import com.lrin.project.service.member.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

@Controller
public class MemberController {
    @Autowired
    MemberService memberService;

    @GetMapping(value = "/login")
    public String login(Model model) {
        model.addAttribute("cssPath", "member/login");
        model.addAttribute("pageTitle", "로그인");
        model.addAttribute("jsPath", "member/member");
        return "member/login";
    }
    @GetMapping(value = "/signup")
    public String signup(Model model) {
        model.addAttribute("cssPath", "member/signup");
        model.addAttribute("pageTitle", "회원가입");
        model.addAttribute("jsPath", "member/member");
        return "member/signup";
    }
    @PostMapping(value = "/memberChk")//아이디 중복 확인
    public void memberChk(@RequestParam("id") String id, HttpServletResponse response) throws IOException {
        String result = memberService.findMember(id);
        PrintWriter pp = response.getWriter();
        pp.print(result);
    }
    @PostMapping(value = "/memberSave")
    public String memberSave(
            @ModelAttribute("memberDTO") @Valid MemberDTO memberDTO) {
        MemberEntity memberEntity = memberDTO.toEntity();
        memberService.memberSave(memberEntity);
        return "redirect:/";
    }
    @PostMapping(value = "/idpwChk")
    public void idpwChk(@RequestParam("id") String id, @RequestParam("pw") String pw, HttpServletResponse response) throws IOException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter prw = response.getWriter();
        String result = memberService.idPwChk(id);
        boolean chk = passwordEncoder.matches(pw,result);
        prw.print(chk);
    }
    @GetMapping(value = "/mypage/list")
    public String mypageList(Model model) {
        model.addAttribute("cssPath", "member/mylist");
        model.addAttribute("pageTitle", "마이페이지 메인");
        model.addAttribute("jsPath", "member/member");
        return "member/mylist";
    }
    @GetMapping(value = "/mypage")
    public String mypage(Model model, Principal principal) {
        model.addAttribute("cssPath", "member/mypage");
        model.addAttribute("pageTitle", "마이페이지");
        model.addAttribute("jsPath", "member/member");
        MemberEntity dto = memberService.memberInfo(principal.getName());
        model.addAttribute("member", dto);
        return "member/mypage";
    }
    @PostMapping(value = "/myUpdate")
    public String myUpdate(
            @ModelAttribute("memberDTO") @Valid MemberDTO memberDTO) {
        MemberEntity memberEntity = memberDTO.toEntity();
        String id = memberDTO.getId();
        String pw = memberDTO.getPw();
        String addr = memberDTO.getAddr();
        String streetaddr = memberDTO.getStreetaddr();
        String detailaddr = memberDTO.getDetailaddr();
        String tel = memberDTO.getTel();
        if(pw == null || pw.isEmpty()){
            memberService.mySomeSave(addr,streetaddr,detailaddr,tel,id);
        }
        else{
            memberService.memberSave(memberEntity);
        }
        return "redirect:/";
    }
    @GetMapping(value = "/find")
    public String find(Model model) {
        model.addAttribute("cssPath", "member/find");
        model.addAttribute("pageTitle", "아이디/비밀번호 찾기");
        model.addAttribute("jsPath", "member/member");
        return "member/find";
    }
    @PostMapping(value = "/idFind")
    public void idFind(@RequestParam("name") String name,
                        @RequestParam("tel") String tel,
                         HttpServletResponse response) throws IOException {
        String result = memberService.idFind(name, tel);
        PrintWriter pp = response.getWriter();
        pp.print(result);
    }
    @PostMapping(value = "/pwFind")
    public void pwFind(@RequestParam("id") String id,
                        @RequestParam("name") String name,
                        @RequestParam("tel") String tel,
                       HttpServletResponse response) throws IOException {
        String result = memberService.pwFind(id, name, tel);
        PrintWriter pp = response.getWriter();
        pp.print(result);
    }
    @GetMapping(value = "password")
    public String password(@RequestParam("id") String id, Model model){
        model.addAttribute("cssPath", "member/password");
        model.addAttribute("pageTitle", "비밀번호 재설정");
        model.addAttribute("jsPath", "member/member");
        model.addAttribute("id", id);
        return "member/password";
    }
    @PostMapping(value = "pwSetting")
    public String pwSetting(@RequestParam("id") String id,
                            @RequestParam("pw") String pw){
        memberService.pwSetting(id, pw);
        return  "redirect:/login";
    }
}
