package roomescape.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class SaveReservationTimeRequestTest {

    @DisplayName("SaveReservationTimeRequest를 ReservationTime으로 변환한다.")
    @Test
    void toReservationTimeTest() {
        // Given
        SaveReservationTimeRequest request = new SaveReservationTimeRequest(LocalTime.of(1, 12));

        // When
        ReservationTime reservationTime = request.toReservationTime();

        // Then
        assertThat(reservationTime).isNotNull();
    }
}
