package roomescape.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SaveReservationRequestTest {

    @DisplayName("SaveReservationRequest를 Reservation으로 변환한다.")
    @Test
    void toReservationTest() {
        // Given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(2, 22));
        SaveReservationRequest request = new SaveReservationRequest(
                LocalDate.now().plusDays(1),
                "켈리",
                1L
        );

        // When
        Reservation reservation = request.toReservation(reservationTime);

        // Then
        assertAll(
                () ->  assertThat(reservation.getDate()).isEqualTo(request.date()),
                () -> assertThat(reservation.getClientName().getValue()).isEqualTo(request.name()),
                () ->  assertThat(reservation.getTime().getId()).isEqualTo(reservationTime.getId()),
                () ->  assertThat(reservation.getTime().getStartAt()).isEqualTo(reservationTime.getStartAt())
        );
    }
}
