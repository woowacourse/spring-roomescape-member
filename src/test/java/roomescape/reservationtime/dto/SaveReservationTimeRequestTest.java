package roomescape.reservationtime.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservationtime.domain.ReservationTime;

class SaveReservationTimeRequestTest {

  @DisplayName("SaveReservationTimeRequest를 ReservationTime으로 변환한다.")
  @Test
  void toReservationTimeTest() {
    // Given
    final LocalTime startAt = LocalTime.of(1, 12);
    final SaveReservationTimeRequest request = new SaveReservationTimeRequest(startAt);

    // When
    final ReservationTime reservationTime = request.toReservationTime();

    // Then
    assertThat(reservationTime).isNotNull();
  }
}
