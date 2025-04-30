package roomescape.reservation.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeId;

import java.time.LocalDate;
import java.time.LocalTime;

class ReservationTest {

    @Test
    @DisplayName("과거 날짜와 시간에 대한 예약 생성은 불가능하다.")
    void validatePast() {
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThatThrownBy(() -> Reservation.withoutId(
                    ReserverName.from("시소"),
                    ReservationDate.from(LocalDate.now().minusDays(1L)),
                    ReservationTime.of(ReservationTimeId.unassigned(), LocalTime.now())
            )).isInstanceOf(IllegalArgumentException.class);
            softAssertions.assertThatThrownBy(() -> Reservation.withoutId(
                    ReserverName.from("시소"),
                    ReservationDate.from(LocalDate.now()),
                    ReservationTime.of(ReservationTimeId.unassigned(), LocalTime.now().minusMinutes(1L))
            )).isInstanceOf(IllegalArgumentException.class);
        });
    }
}
