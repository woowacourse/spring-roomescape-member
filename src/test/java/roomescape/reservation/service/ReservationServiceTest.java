package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.payload.ReservationRequest;

@Sql({"/create_reservation_time.sql", "/create_theme.sql"})
@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void 예약요청을_올바르게_저장하는지_확인하는_테스트() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2026, 5, 6), 1L, 1L);
        Reservation reservation = reservationService.save(reservationRequest);
        assertThat(reservation.getName()).isEqualTo(reservationRequest.name());
        assertThat(reservation.getDate()).isEqualTo(reservationRequest.date());
        assertThat(reservation.getTime().getId()).isEqualTo(reservationRequest.timeId());
        assertThat(reservation.getTheme().getId()).isEqualTo(reservationRequest.themeId());
    }

    @Test
    void 예약목록을_올바르게_조회하는지_확인하는_테스트() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2026, 5, 6), 1L, 1L);
        Reservation reservation = reservationService.save(reservationRequest);

        List<Reservation> reservations = reservationService.findAll();

        assertThat(reservations).contains(reservation);
    }

    @Test
    void 예약을_올바르게_삭제하는지_확인하는_테스트() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2026, 5, 6), 1L, 1L);
        Reservation reservation = reservationService.save(reservationRequest);

        reservationService.deleteById(reservation.getId());

        List<Reservation> reservations = reservationService.findAll();
        assertThat(reservations).doesNotContain(reservation);
    }
}
