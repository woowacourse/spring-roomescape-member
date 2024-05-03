package roomescape;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import roomescape.service.util.PastDateTimeFormatter;
import roomescape.service.util.DateTimeFormatter;

@TestConfiguration
public class TestConfig {

    @Primary
    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return new PastDateTimeFormatter();
    }
}
