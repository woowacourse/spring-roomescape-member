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
import roomescape.reservation.exception.PastReservationNotAllowedException;
import roomescape.reservation.exception.ReservationAccessDeniedException;
import roomescape.reservation.exception.ReservationDuplicatedException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.payload.ReservationRequest;
import roomescape.reservation.payload.ReservationUpdateRequest;
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
    void 이름으로_예약목록을_조회한다() {
        ReservationRequest reservationRequest1 = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 1L);
        ReservationRequest reservationRequest2 = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 2L, 1L);
        ReservationRequest otherReservationRequest = new ReservationRequest("밀란", LocalDate.of(2099, 5, 6), 3L, 1L);
        Reservation reservation1 = reservationService.save(reservationRequest1);
        Reservation reservation2 = reservationService.save(reservationRequest2);
        reservationService.save(otherReservationRequest);

        List<Reservation> reservations = reservationService.findByName("봉구스");

        assertThat(reservations).containsExactly(reservation2, reservation1);
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

    @Test
    void 예약자_이름이_같으면_예약을_취소한다() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 1L);
        Reservation reservation = reservationService.save(reservationRequest);

        reservationService.cancelByIdAndName(reservation.getId(), "봉구스");

        List<Reservation> reservations = reservationService.findAll();
        assertThat(reservations).doesNotContain(reservation);
    }

    @Test
    void 없는_예약을_이름으로_취소하면_에러를_던진다() {
        assertThatThrownBy(() -> reservationService.cancelByIdAndName(999L, "봉구스"))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void 예약자_이름이_다르면_예약을_취소할_수_없다() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 1L);
        Reservation reservation = reservationService.save(reservationRequest);

        assertThatThrownBy(() -> reservationService.cancelByIdAndName(reservation.getId(), "다른이름"))
                .isInstanceOf(ReservationAccessDeniedException.class);
    }

    @Test
    void 예약을_수정한다() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 1L);
        Reservation reservation = reservationService.save(reservationRequest);
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.of(2099, 5, 7), 2L, 2L);

        Reservation updatedReservation = reservationService.updateByIdAndName(
                reservation.getId(),
                "봉구스",
                updateRequest
        );

        assertThat(updatedReservation.getId()).isEqualTo(reservation.getId());
        assertThat(updatedReservation.getName()).isEqualTo("봉구스");
        assertThat(updatedReservation.getDate()).isEqualTo(LocalDate.of(2099, 5, 7));
        assertThat(updatedReservation.getTime().getId()).isEqualTo(2L);
        assertThat(updatedReservation.getTheme().getId()).isEqualTo(2L);
    }

    @Test
    void 수정할_예약이_없으면_에러를_던진다() {
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.of(2099, 5, 7), null, null);

        assertThatThrownBy(() -> reservationService.updateByIdAndName(999L, "봉구스", updateRequest))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void 예약자_이름이_다르면_예약을_수정할_수_없다() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 1L);
        Reservation reservation = reservationService.save(reservationRequest);
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.of(2099, 5, 7), null, null);

        assertThatThrownBy(() -> reservationService.updateByIdAndName(reservation.getId(), "다른이름", updateRequest))
                .isInstanceOf(ReservationAccessDeniedException.class);
    }

    @Test
    void 존재하지_않는_예약시간으로_수정하면_에러를_던진다() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 1L);
        Reservation reservation = reservationService.save(reservationRequest);
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(null, 999L, null);

        assertThatThrownBy(() -> reservationService.updateByIdAndName(reservation.getId(), "봉구스", updateRequest))
                .isInstanceOf(ReservationTimeNotFoundException.class);
    }

    @Test
    void 존재하지_않는_테마로_수정하면_에러를_던진다() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 1L);
        Reservation reservation = reservationService.save(reservationRequest);
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.of(2099, 5, 6), 1L, 999L);

        assertThatThrownBy(() -> reservationService.updateByIdAndName(reservation.getId(), "봉구스", updateRequest))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @Test
    void 과거_날짜_시간으로_수정하면_에러를_던진다() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 1L);
        Reservation reservation = reservationService.save(reservationRequest);
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.of(2000, 1, 1), 1L, 1L);

        assertThatThrownBy(() -> reservationService.updateByIdAndName(reservation.getId(), "봉구스", updateRequest))
                .isInstanceOf(PastReservationNotAllowedException.class);
    }

    @Test
    void 이미_예약된_날짜_시간_테마로_수정하면_에러를_던진다() {
        ReservationRequest reservationRequest1 = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 1L);
        ReservationRequest reservationRequest2 = new ReservationRequest("밀란", LocalDate.of(2099, 5, 7), 2L, 1L);
        reservationService.save(reservationRequest1);
        Reservation reservation = reservationService.save(reservationRequest2);
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.of(2099, 5, 6), 1L, 1L);

        assertThatThrownBy(() -> reservationService.updateByIdAndName(reservation.getId(), "밀란", updateRequest))
                .isInstanceOf(ReservationDuplicatedException.class);
    }

    @Test
    void 수정할_값이_없으면_에러를_던진다() {
        ReservationRequest reservationRequest = new ReservationRequest("봉구스", LocalDate.of(2099, 5, 6), 1L, 1L);
        Reservation reservation = reservationService.save(reservationRequest);
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(null, null, null);

        assertThatThrownBy(() -> reservationService.updateByIdAndName(reservation.getId(), "봉구스", updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("변경할 예약 정보가 없습니다.");
    }
}
