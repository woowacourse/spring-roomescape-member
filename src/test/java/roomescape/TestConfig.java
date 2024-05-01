package roomescape;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import roomescape.service.util.DateTimeConfiguration;
import roomescape.service.util.DateTimeFormatter;

@MockBean(classes = DateTimeConfiguration.class)
@TestConfiguration
public class TestConfig {

    @Bean
    public DateTimeFormatter getTestDateTimeFormatter() {
        return new DateTimeFormatter() {
            @Override
            public LocalDate getDate() {
                return LocalDate.parse("2000-01-01");
            }

            @Override
            public LocalTime getTime() {
                return LocalTime.parse("01:00");
            }
        };
    }

}
