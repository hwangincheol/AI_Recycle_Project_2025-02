package com.lrin.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
// 이미지, 동영상 처리 확인용 html용 controller
public class PythonTmpController {
    @GetMapping(value = "/python")
    public String image(){
        return "python/image";
    }//end image
}//end class
