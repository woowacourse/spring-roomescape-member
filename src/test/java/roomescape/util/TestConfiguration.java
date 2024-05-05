package roomescape.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import roomescape.service.util.DateTimeFormatter;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {
    @Bean
    @Primary
    public DateTimeFormatter getOldDateTimeFormater() {
        return new OldDateTimeFormatter();
    }
}
