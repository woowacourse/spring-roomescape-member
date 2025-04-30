package roomescape.domain_entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    private final String TEST_NAME = "moda";

    @Test
    @DisplayName("지난 날짜와 시간 예약일 경우 예외가 발생한다")
    void failIfPastDateOrPastTime() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(
                new Id(1L),
                TEST_NAME,
                now.toLocalDate().minusDays(1),
                new ReservationTime(now.toLocalTime())
        );

        //when & then
        assertThatThrownBy(() -> reservation.validatePastDateTime())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지난 날짜와 시간의 예약은 생성 불가능합니다.");
    }

}