package roomescape.service.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class DateTimeConfiguration {

    @Bean
    public DateTimeFormatter getNowDateTimeFormatter() {
        return new NowDateTimeFormatter();
    }
}
