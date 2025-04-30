package roomescape.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.testFixture.Fixture.THEME_1;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.application.dto.ReservationDto;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.presentation.dto.request.ReservationRequest;
import roomescape.presentation.dto.response.AdminReservationResponse;

class ReservationMapperTest {

    @DisplayName("예약 요청을 Reservation 객체로 변환한다.")
    @Test
    void request_toReservation() {
        // given
        LocalDate reservationDate = LocalDate.of(2024, 4, 1);
        ReservationTime reservationTime = ReservationTime.of(1L, LocalTime.of(10, 0));
        ReservationRequest request = new ReservationRequest(1L, reservationDate, "멍구", 1L);

        // when
        Reservation reservation = ReservationMapper.toDomain(request, THEME_1, reservationTime);

        // then
        assertAll(
                () -> assertThat(reservation.getId()).isNull(),
                () -> assertThat(reservation.getName()).isEqualTo("멍구"),
                () -> assertThat(reservation.getReservationDate()).isEqualTo(reservationDate),
                () -> assertThat(reservation.getReservationTime()).isEqualTo(reservationTime)
        );
    }

    @DisplayName("Reservation 객체를 응답객체로 변환한다.")
    @Test
    void reservation_toResponse() {
        // given
        Long timeId = 1L;
        String name = "멍구";

        LocalDate reservationDate = LocalDate.of(2025, 4, 1);
        ReservationTime reservationTime = ReservationTime.of(timeId, LocalTime.of(10, 0));
        Reservation reservation = Reservation.of(timeId, name, THEME_1, reservationDate, reservationTime);

        // when
        ReservationDto dto = ReservationMapper.toDto(reservation);

        // then
        assertAll(
                () -> assertThat(dto.id()).isEqualTo(timeId),
                () -> assertThat(dto.name()).isEqualTo(name),
                () -> assertThat(dto.date()).isEqualTo(reservationDate),
                () -> assertThat(dto.time().id()).isEqualTo(timeId)
        );
    }

    @DisplayName("Reservation 객체들을 응답 리스트로 한꺼번에 변환한다.")
    @Test
    void multipleReservations_toResponse() {
        // given
        ReservationTime time1 = ReservationTime.of(1L, LocalTime.of(10, 0));
        ReservationTime time2 = ReservationTime.of(2L, LocalTime.of(11, 0));

        Reservation reservation1 = Reservation.of(1L, "브라운", THEME_1, LocalDate.of(2024, 4, 1), time1);
        Reservation reservation2 = Reservation.of(2L, "솔라", THEME_1, LocalDate.of(2024, 4, 1), time2);
        Reservation reservation3 = Reservation.of(3L, "브리", THEME_1, LocalDate.of(2024, 4, 2), time1);

        List<Reservation> reservations = List.of(reservation1, reservation2, reservation3);

        // when
        List<ReservationDto> dtos = ReservationMapper.toDtos(reservations);

        // then
        assertAll(
                () -> assertThat(dtos).hasSize(3),
                () -> assertThat(dtos)
                        .extracting(ReservationDto::name)
                        .containsExactly("브라운", "솔라", "브리")
        );
    }
}
