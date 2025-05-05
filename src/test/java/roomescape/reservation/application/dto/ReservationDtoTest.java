package roomescape.reservation.application.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static roomescape.testFixture.Fixture.RESERVATION_1;
import static roomescape.testFixture.Fixture.RESERVATION_2;
import static roomescape.testFixture.Fixture.RESERVATION_3;
import static roomescape.testFixture.Fixture.THEME_1;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;
import roomescape.reservation.application.dto.info.ReservationDto;

class ReservationDtoTest {
    @DisplayName("Reservation 객체를 응답객체로 변환한다.")
    @Test
    void reservation_toResponse() {
        // given
        Long timeId = 1L;
        String name = "멍구";
        long memberId = 1L;

        LocalDate reservationDate = LocalDate.of(2025, 4, 1);
        ReservationTime reservationTime = ReservationTime.of(timeId, LocalTime.of(10, 0));
        Reservation reservation = Reservation.of(timeId, memberId, THEME_1, reservationDate, reservationTime);

        // when
        ReservationDto dto = ReservationDto.from(reservation);

        // then
        assertAll(
                () -> assertThat(dto.id()).isEqualTo(timeId),
                () -> assertThat(dto.memberId()).isEqualTo(memberId),
                () -> assertThat(dto.date()).isEqualTo(reservationDate),
                () -> assertThat(dto.time().id()).isEqualTo(timeId)
        );
    }

    @DisplayName("Reservation 객체들을 응답 리스트로 한꺼번에 변환한다.")
    @Test
    void multipleReservations_toResponse() {
        // given
        List<Reservation> reservations = List.of(RESERVATION_1, RESERVATION_2, RESERVATION_3);

        // when
        List<ReservationDto> dtos = ReservationDto.from(reservations);

        // then
        assertAll(
                () -> assertThat(dtos).hasSize(3),
                () -> assertThat(dtos)
                        .extracting(ReservationDto::id)
                        .containsExactly(RESERVATION_1.getId(), RESERVATION_2.getId(), RESERVATION_3.getId())
        );
    }

}
