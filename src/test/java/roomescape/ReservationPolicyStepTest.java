package roomescape;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Time;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.exception.BusinessRuleViolationException;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationCreateCommand;

/*
 * 미션2 사이클2 - 서비스 정책 검증 테스트.
 *
 * IntegrationTest 상속으로 매 테스트 빈 DB 보장.
 * 고정 Clock(2026-05-13 12:00)을 주입해 "과거/현재/미래"의 의미를 결정적으로 만듦.
 *
 * 각 @Nested 클래스가 정책 하나에 대응 — 정책이 늘어나면 @Nested가 늘어남 !
 */
public class ReservationPolicyStepTest extends IntegrationTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 5, 13);
    private static final LocalTime NOW_TIME = LocalTime.of(12, 0);

    @TestConfiguration
    static class FixedClockConfig {
        @Bean
        @Primary
        public Clock fixedClock() {
            return Clock.fixed(
                    TODAY.atTime(NOW_TIME).atZone(ZoneId.systemDefault()).toInstant(),
                    ZoneId.systemDefault()
            );
        }
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationService reservationService;

    private Long timeId10;
    private Long themeId;

    @BeforeEach
    void setUp() {
        timeId10 = insertTime(LocalTime.of(10, 0));
        themeId = insertTheme("테마A", "설명", "https://example.com/a.jpg");
    }

    @Nested
    @DisplayName("과거 시점 예약 거부 정책")
    class PastReservationPolicy {

        @Test
        @DisplayName("어제 날짜로 예약 시도 시 거부된다")
        void 어제_날짜는_거부() {
            ReservationCreateCommand command = new ReservationCreateCommand(
                    "브라운", TODAY.minusDays(1), timeId10, themeId
            );

            assertThatThrownBy(() -> reservationService.create(command))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasMessage("지나간 날짜,시간으로는 예약할 수 없습니다.");
        }

        @Test
        @DisplayName("오늘 + 이미 지난 시간(10:00, 현재 12:00)은 거부된다")
        void 오늘이지만_이미_지난_시간은_거부() {
            ReservationCreateCommand command = new ReservationCreateCommand(
                    "브라운", TODAY, timeId10, themeId
            );

            assertThatThrownBy(() -> reservationService.create(command))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasMessage("지나간 날짜,시간으로는 예약할 수 없습니다.");
        }

        @Test
        @DisplayName("오늘 + 미래 시간(15:00, 현재 12:00)은 허용된다")
        void 오늘이지만_미래_시간은_허용() {
            Long timeId15 = insertTime(LocalTime.of(15, 0));
            ReservationCreateCommand command = new ReservationCreateCommand(
                    "브라운", TODAY, timeId15, themeId
            );

            assertThatCode(() -> reservationService.create(command))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("내일 날짜는 허용된다")
        void 내일_날짜는_허용() {
            ReservationCreateCommand command = new ReservationCreateCommand(
                    "브라운", TODAY.plusDays(1), timeId10, themeId
            );

            assertThatCode(() -> reservationService.create(command))
                    .doesNotThrowAnyException();
        }
    }

    private Long insertTime(LocalTime startAt) {
        jdbcTemplate.update(
                "INSERT INTO reservation_time (start_at) VALUES (?)",
                Time.valueOf(startAt));
        return jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_at = ?",
                Long.class, Time.valueOf(startAt));
    }

    private Long insertTheme(String name, String description, String thumbnailUrl) {
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)",
                name, description, thumbnailUrl);
        return jdbcTemplate.queryForObject(
                "SELECT id FROM theme WHERE name = ?",
                Long.class, name);
    }
}
