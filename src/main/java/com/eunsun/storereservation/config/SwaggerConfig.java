package com.eunsun.storereservation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // http://localhost:8080/swagger-ui/index.html

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("매장 예약 프로젝트")
                        .description("고객이 원하는 날짜와 시간에 매장 방문을 예약할 수 있고, 매장 점주가 효율적으로 예약을 관리할 수 있도록 도와주는 예약 시스템입니다.")
                        .version("1.0")
                );
    }
}