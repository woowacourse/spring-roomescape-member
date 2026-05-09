package roomescape.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Roomescape API")
                        .description("방탈출 예약 관리 시스템 REST API 문서\n\n"
                                + "- **사용자 API**: `/reservations`, `/times`, `/themes`\n"
                                + "- **관리자 API**: `/admin/reservations`, `/admin/times`, `/admin/themes`")
                        .version("v1.0.0"));
    }
}
