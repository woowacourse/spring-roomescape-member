package roomescape.domain.reservation.entity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

class ReservationTest {

    @Nested
    @DisplayName("예약 생성 테스트")
    class Create {

        Clock fixedClock;

        @BeforeEach
        void setUp() {
            this.fixedClock = Clock.fixed(
                Instant.parse("2026-05-12T10:00:00Z"),
                ZoneId.of("UTC")
            );
        }

        @Test
        @DisplayName("정상 테스트")
        void 성공() {
            LocalDate date = LocalDate.of(2026, 5, 20);
            Time time = Time.create(LocalTime.of(20, 30));
            Theme theme = Theme.create("성", "성 테마 설명", "castle_image_url");

            assertThatCode(() -> Reservation.create("브라운", date, time, theme, fixedClock))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("이전 날짜로 예약을 시도하면 예외가 발생한다.")
        void 실패1() {
            LocalDate date = LocalDate.of(2026, 5, 11);
            Time time = Time.create(LocalTime.of(20, 30));
            Theme theme = Theme.create("성", "성 테마 설명", "castle_image_url");

            assertThatThrownBy(() -> Reservation.create("브라운", date, time, theme, fixedClock))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이전 날짜로 예약할 수 없습니다.");
        }
    }

}