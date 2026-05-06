package roomescape.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

class ReservationTimeTest {

    @Test
    @DisplayName("정상 예약 시간 생성")
    void createNew_Success() {
        // given
        LocalTime time = LocalTime.parse("10:00");
        Theme theme = Theme.of(1L, "미술관의 밤", "추리 테마", "https://example.com/theme.png");

        // when
        ReservationTime reservationTime = ReservationTime.createNew(time, theme);

        // then
        assertThat(reservationTime.getId()).isNull();
        assertThat(reservationTime.getStartAt()).isEqualTo(time);
        assertThat(reservationTime.getTheme()).isEqualTo(theme);
    }

    @Test
    @DisplayName("예약 시간 null 예외")
    void validate_NullStartAt_ThrowsException() {
        // given
        LocalTime nullTime = null;
        Theme theme = Theme.of(1L, "미술관의 밤", "추리 테마", "https://example.com/theme.png");

        // when & then
        assertThatThrownBy(() -> ReservationTime.createNew(nullTime, theme))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
