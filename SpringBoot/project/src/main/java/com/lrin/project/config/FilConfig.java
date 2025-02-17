package com.lrin.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}") // application.properties에서 경로를 가져옴
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 상대경로를 설정하여 브라우저에서 접근할 URL 경로 설정
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./" + uploadDir + "/");
    }
}
