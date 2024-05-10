package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.auth.domain.Member;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

class ReservationTest {

  @DisplayName("인덱스를 입력하면 해당 아이디를 가진 Reservation 객체를 생성해서 반환한다.")
  @Test
  void initializeIndex() {
    // Given
    final String clientName = "켈리";
    final LocalDate reservationDate = LocalDate.now().plusDays(1);
    final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(2, 22));
    final Theme theme = Theme.of(1L, "테바의 비밀친구", "테바의 은밀한 비밀친구", "대충 테바 사진 링크");
    final Reservation reservation = Reservation.createInstanceWithoutId(
        Member.of(new Name(clientName)),
        reservationDate,
        reservationTime,
        theme
    );
    final Long initialIndex = 3L;

    // When
    final Reservation initIndexReservation = reservation.copyWithId(initialIndex);

    // Then
    assertThat(initIndexReservation.getId()).isEqualTo(initialIndex);
  }

  @DisplayName("현재 날짜/시간보다 이전의 예약 정보를 입력하면 예외가 발생한다.")
  @Test
  void throwExceptionWithReservationDateTimeBeforeNow() {
    // Given
    final ReservationTime reservationTime = new ReservationTime(LocalTime.now());
    final Theme theme = Theme.of(1L, "테바의 비밀친구", "테바의 은밀한 비밀친구", "대충 테바 사진 링크");
    final LocalDate dateBeforeNow = LocalDate.now().minusDays(1);

    // When & Then
    assertThatThrownBy(
        () -> Reservation.createInstanceWithoutId(Member.of(new Name("켈리")), dateBeforeNow,
            reservationTime, theme))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("예약 일시는 현재 시간 이후여야 합니다.");
  }
}
