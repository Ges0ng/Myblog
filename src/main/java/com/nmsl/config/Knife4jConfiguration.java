package com.nmsl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author paracosm
 * @version 1.0
 * @date 2021/4/5 16:11
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("# Paracosm Blog APIs")
                        .description("# Paracosm Blog APIs")
                        .termsOfServiceUrl("http://blog.paracosm.top/")
                        .contact("704965520@qq.com")
                        .version("1.3")
                        .build())
                //分组名称
                .groupName("1.3版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.nmsl.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
