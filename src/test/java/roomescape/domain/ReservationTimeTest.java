package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTimeTest {

    @DisplayName("인덱스를 입력하면 해당 아이디를 가진 ReservationTime 객체를 생성해서 반환한다.")
    @Test
    void initializeIndex() {
        // Given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(1, 12));
        Long initialIndex = 3L;

        // When
        ReservationTime initIndexReservationTime = reservationTime.initializeIndex(initialIndex);

        // Then
        assertThat(initIndexReservationTime.getId()).isEqualTo(initialIndex);
    }
}
