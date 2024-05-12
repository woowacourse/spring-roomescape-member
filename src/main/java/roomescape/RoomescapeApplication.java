package roomescape;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import roomescape.infrastructure.JwtTokenProvider;

@SpringBootApplication
@EnableConfigurationProperties(JwtTokenProvider.class)
public class RoomescapeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomescapeApplication.class, args);
    }

}
