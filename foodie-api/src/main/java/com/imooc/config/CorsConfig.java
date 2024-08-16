package com.imooc.config;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@NoArgsConstructor
public class CorsConfig {

    @Bean
    public CorsFilter getCorsFilter() {
        // 1. CORS 配置信息
        CorsConfiguration config = new CorsConfiguration();
        // 允许的 origin
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://192.168.0.195:8080");
        config.addAllowedOrigin("http://server.foodie.com:8080");
        // 允许的 Header
        config.addAllowedHeader("*");
        // 允许的请求方法，如 GET、POST
        config.addAllowedMethod("*");
        // 允许 Cookie
        config.setAllowCredentials(true);

        // 2. 为 URL 添加映射路径 (为 /** 配置的跨域)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        // 3. 返回新的 CorsFilter
        return new CorsFilter(source);
    }
}
