package roomescape.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationResponseTest {

    @DisplayName("Reservation을 입력받으면 ReservationResponse로 변환한다.")
    @Test
    void convertDtoTest() {
        // Given
        final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(2, 22));
        final Theme theme = Theme.of(1L, "테바의 비밀친구", "테바의 은밀한 비밀친구", "대충 테바 사진 링크");
        final String clientName = "켈리";
        final LocalDate reservationDate = LocalDate.now().plusDays(1);
        final Reservation reservation = Reservation.of(1L, clientName, reservationDate, reservationTime, theme);

        // When
        final ReservationResponse reservationResponse = ReservationResponse.from(reservation);

        // Then
        assertAll(
                () -> assertThat(reservationResponse).isNotNull(),
                () -> assertThat(reservationResponse.id()).isEqualTo(reservation.getId()),
                () -> assertThat(reservationResponse.name()).isEqualTo(reservation.getClientName().value()),
                () -> assertThat(reservationResponse.date()).isEqualTo(reservation.getDate().value()),
                () -> assertThat(reservationResponse.time().startAt()).isEqualTo(reservation.getTime().getStartAt())
        );
    }
}
