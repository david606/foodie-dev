package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    /**
     * 配置 Swagger2 核心配置
     * <br>
     * <a href="http://localhost:8088/swagger-ui.html">官方风格</a>
     * <a href="http://localhost:8088/doc.html">三方风格</a>
     * <p>
     * 通过此方法创建并配置Swagger2的Docket对象，用于生成API文档。
     * <br>
     * 主要配置项包括API的类型、基本信息、Controller扫描包及路径选择。
     * </p>
     *
     * @return 配置好的Docket对象，用于生成Swagger2的API文档。
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)  // 指定 API 类型为 Swagger2
                .apiInfo(apiInfo())                     // 指定 API 文档汇总信息
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.imooc.controller")) // 指定 Controller 扫描包
                .paths(PathSelectors.any())             // 扫描所有
                .build();
    }

    /**
     * 配置API文档的元信息
     *
     * @return ApiInfo对象，包含API的基本信息如标题、描述、联系人等
     */
    private ApiInfo apiInfo() {
        // 创建ApiInfo构建器
        ApiInfoBuilder builder = new ApiInfoBuilder();
        // 设置API文档的标题
        builder.title("Foodie-API")
                // 设置API文档的描述
                .description("Foodie-API 接口文档")
                // 设置服务条款的URL
                .termsOfServiceUrl("https://www.imooc.com")
                // 设置联系人信息，包括姓名、URL和电子邮件
                .contact(new Contact("David", "https://www.imooc.com", "test@163.com"))
                // 设置API文档的版本
                .version("1.0");
        // 构建并返回ApiInfo对象
        return builder.build();
    }

}
