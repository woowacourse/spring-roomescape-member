package roomescape;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.auth.service.AuthService;

@SpringBootApplication
@EnableConfigurationProperties(JwtTokenProvider.class)
public class RoomescapeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomescapeApplication.class, args);
    }

}
