package com.lifespace;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	// 建立一個全域 CORS 設定類別
	
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")  // 開發階段可以允許所有來源（*），但正式上線時請限制可信的來源，避免資安問題
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
            }
        };
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/event-images/**")
        .addResourceLocations("file:D:/tiba_project/event_images/");// 實際目錄位置
        registry.addResourceHandler("/space-comment-images/**")
        .addResourceLocations("file:D:/tiba_project/space_comment_images/");// 實際目錄位置
    }
}

