package roomescape.global;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ClockConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public String insertTestData(JdbcTemplate jdbcTemplate) {
        // reservation_time 데이터
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "11:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "12:00");

        // theme 데이터
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/image.jpg");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "지하 감옥", "깊은 감옥에서 탈출하라!", "https://example.com/jail.jpg");

        // reservation 데이터
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "홍길동", "2025-04-30", 1, 1);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "김철수", "2025-04-30", 2, 1);

        return "";
    }
}
