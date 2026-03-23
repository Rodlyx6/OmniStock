package com.omnistock.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger配置
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OmniStock 仓储管理系统 API")
                        .version("1.0.0")
                        .description("企业级仓储管理系统")
                        .contact(new Contact()
                                .name("技术支持")
                                .email("support@omnistock.com")));
    }
}
