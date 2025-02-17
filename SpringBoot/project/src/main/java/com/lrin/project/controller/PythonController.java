package com.lrin.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class PythonController {

    @Autowired
    private WebClient webClient;

    @PostMapping("/image_service")
    public String imageRequest(MultipartFile file){
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", file.getResource()); //폼데이터, 파일
        String result = webClient.post()
                .uri("/image_service")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return result;
    }//end imageRequest

    @PostMapping("/video_service")
    public String videoRequest(@RequestParam("video_file") MultipartFile files){
        if (files.isEmpty()) {
            return "파일이 비어있습니다.";
        }

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("video_file", files.getResource()); //폼데이터, 파일
        String result = webClient.post()
                .uri("/video_service")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return result;
    }//end imageRequest


}//end class
