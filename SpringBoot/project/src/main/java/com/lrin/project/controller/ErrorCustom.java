package com.lrin.project.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorCustom implements ErrorController {
    @RequestMapping("/error")
    public String error(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            if(statusCode == HttpStatus.FORBIDDEN.value()) {
                //'권한이 없는 페이지'일 경우 띄워줌.
                return "error/403";
            }
            else if(statusCode == HttpStatus.NOT_FOUND.value()) {
                //'페이지를 찾을 수 없음'일 경우 띄워줌.
                return "error/404";
            }
            else if(statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                //'허용되지 않은 메소드'일 경우 띄워줌.
                //(주로 POST/GET에서 많이 발생.)
                return "error/405";
            }
            else{
                //'내부 서버 오류'인 경우 띄워줌.
                //(자주 발생하는 오류로 서버 내부에서 문제가 생겼지만 알 수 없을 때 발생.)
                return "error/500";
            }
        }
        return "error";
    }
}