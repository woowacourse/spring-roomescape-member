package roomescape.reservation.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.auth.domain.Member;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

class SaveReservationRequestTest {

  @DisplayName("SaveReservationRequest를 Reservation으로 변환한다.")
  @Test
  void toReservationTest() {
    // Given
    final LocalDate reservationDate = LocalDate.now().plusDays(1);
    final String clientName = "켈리";
    final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(2, 22));
    final Theme theme = Theme.of(1L, "테바의 비밀친구", "테바의 은밀한 비밀친구", "대충 테바 사진 링크");
    final SaveReservationRequest request = new SaveReservationRequest(
        reservationDate,
        1L,
        1L
    );

    // When
    final Reservation reservation = request.toReservation(Member.of(new Name("켈리")),
        reservationTime, theme);

    // Then
    assertAll(
        () -> assertThat(reservation.getDate().getValue()).isEqualTo(request.date()),
        () -> assertThat(reservation.getMember().getName().getValue()).isEqualTo(clientName),
        () -> assertThat(reservation.getTime().getId()).isEqualTo(reservationTime.getId()),
        () -> assertThat(reservation.getTime().getStartAt()).isEqualTo(reservationTime.getStartAt())
    );
  }
}
