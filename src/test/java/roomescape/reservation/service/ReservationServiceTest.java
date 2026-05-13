package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.exception.ReservationDuplicatedException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.payload.ReservationRequest;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.theme.exception.ThemeNotFoundException;

@Sql({"/create_reservation_time.sql", "/create_theme.sql"})
@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void 예약요청을_올바르게_저장하는지_확인하는_테스트() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 1L);
        Reservation reservation = reservationService.save(reservationRequest);
        assertThat(reservation.getName()).isEqualTo(reservationRequest.name());
        assertThat(reservation.getDate()).isEqualTo(reservationRequest.date());
        assertThat(reservation.getTime().getId()).isEqualTo(reservationRequest.timeId());
        assertThat(reservation.getTheme().getId()).isEqualTo(reservationRequest.themeId());
    }

    @Test
    void 없는_예약시간_id를_입력하면_에러를_던진다() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 999L, 1L);
        assertThatThrownBy(() -> reservationService.save(reservationRequest))
                .isInstanceOf(ReservationTimeNotFoundException.class);
    }

    @Test
    void 없는_테마_id를_입력하면_에러를_던진다() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 999L);
        assertThatThrownBy(() -> reservationService.save(reservationRequest))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @Test
    void 같은_날짜_같은_시간_다른_테마는_예약이_가능하다() {
        ReservationRequest reservationRequest1 = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 1L);
        ReservationRequest reservationRequest2 = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 2L);
        Reservation reservation1 = reservationService.save(reservationRequest1);
        Reservation reservation2 = reservationService.save(reservationRequest2);

        List<Reservation> reservations = reservationService.findAll();
        assertThat(reservations).contains(reservation1, reservation2);
    }

    @Test
    void 이미_있는_예약을_다시_생성하면_에러를_던진다() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 1L);
        reservationService.save(reservationRequest);

        assertThatThrownBy(() -> reservationService.save(reservationRequest))
                .isInstanceOf(ReservationDuplicatedException.class);
    }

    @Test
    void 예약목록을_올바르게_조회하는지_확인하는_테스트() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 1L);
        Reservation reservation = reservationService.save(reservationRequest);

        List<Reservation> reservations = reservationService.findAll();

        assertThat(reservations).contains(reservation);
    }

    @Test
    void 예약을_올바르게_삭제하는지_확인하는_테스트() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 1L);
        Reservation reservation = reservationService.save(reservationRequest);

        reservationService.deleteById(reservation.getId());

        List<Reservation> reservations = reservationService.findAll();
        assertThat(reservations).doesNotContain(reservation);
    }

    @Test
    void 없는_예약을_삭제하면_에러를_던진다() {
        assertThatThrownBy(() -> reservationService.deleteById(999L))
                .isInstanceOf(ReservationNotFoundException.class);
    }
}
