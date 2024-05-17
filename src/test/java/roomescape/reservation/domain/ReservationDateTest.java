package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationDateTest {

  @DisplayName("예약 날짜에 Null을 입력하면 예외를 발생한다.")
  @Test
  void throwExceptionWhenReservationDateNullTest() {
    // When & Then
    assertThatThrownBy(() -> new ReservationDate(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("예약 날짜는 공백을 입력할 수 없습니다.");
  }
}
