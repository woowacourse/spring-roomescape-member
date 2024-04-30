package roomescape.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ClientName;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationResponseTest {

    @DisplayName("Reservation을 입력받으면 ReservationResponse로 변환한다.")
    @Test
    void convertDtoTest() {
        // Given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(2, 22));
        Reservation reservation = new Reservation(
                1L,
                new ClientName("켈리"),
                LocalDate.now().plusDays(1),
                reservationTime
        );

        // When
        ReservationResponse reservationResponse = ReservationResponse.from(reservation);

        // Then
        assertAll(
                () -> assertThat(reservationResponse).isNotNull(),
                () -> assertThat(reservationResponse.id()).isEqualTo(reservation.getId()),
                () -> assertThat(reservationResponse.name()).isEqualTo(reservation.getClientName().getValue()),
                () -> assertThat(reservationResponse.date()).isEqualTo(reservation.getDate()),
                () -> assertThat(reservationResponse.time().startAt()).isEqualTo(reservation.getTime().getStartAt())
        );
    }
}
