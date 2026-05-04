package roomescape.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.Time;
import roomescape.dto.ReservationRequestDto;
import roomescape.service.fake.FakeReservationDao;
import roomescape.service.fake.FakeTimeDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private ReservationService reservationService;
    private FakeReservationDao reservationDao;
    private FakeTimeDao timeDao;

    @BeforeEach
    void setUp() {
        reservationDao = new FakeReservationDao();
        timeDao = new FakeTimeDao();
        reservationService = new ReservationService(reservationDao, timeDao);

        timeDao.insert(new Time(LocalTime.of(13, 0)));
        timeDao.insert(new Time(LocalTime.of(14, 0)));
    }

    @Test
    void 예약을_생성하면_id가_부여된_예약이_생성된다() {
        Reservation reservation = reservationService.create(new ReservationRequestDto(
                "유저1", LocalDate.of(2026, 5, 3), 1L));
        System.out.println(reservation.getId());
        assertThat(reservation.getId()).isEqualTo(1L);
    }

    @Test
    void 예약을_생성하려는_시간이_존재하지_않는다면_예외_처리한다() {
        Long timeId = 3L;

        Assertions.assertThatThrownBy(() -> {
            Reservation reservation = reservationService.create(new ReservationRequestDto(
                    "유저1", LocalDate.of(2026, 5, 3), timeId));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 지정한_id를_가진_예약을_조회할_수_있다() {
        Reservation reservation1 = reservationService.create(new ReservationRequestDto(
                "유저1", LocalDate.of(2026, 5, 3), 1L));
        Reservation reservation2 = reservationService.create(new ReservationRequestDto(
                "유저1", LocalDate.of(2026, 5, 4), 2L));
        Long id = 1L;

        Reservation reservationById = reservationService.findById(id);

        assertThat(reservation1).isEqualTo(reservationById);
    }

    @Test
    void 조회할_id가_존재하지_않으면_얘외처리한다() {
        Reservation reservation1 = reservationService.create(new ReservationRequestDto(
                "유저1", LocalDate.of(2026, 5, 3), 1L));
        Reservation reservation2 = reservationService.create(new ReservationRequestDto(
                "유저1", LocalDate.of(2026, 5, 4), 2L));
        Long id = 3L;

        assertThatThrownBy(() -> {
            Reservation reservationById = reservationService.findById(id);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_예약을_요청하면_현재_있는_예약들이_조회된다() {
        Reservation reservation1 = reservationService.create(new ReservationRequestDto(
                "유저1", LocalDate.of(2026, 5, 3), 1L));
        Reservation reservation2 = reservationService.create(new ReservationRequestDto(
                "유저1", LocalDate.of(2026, 5, 4), 2L));

        List<Reservation> reservations = reservationService.findAll();

        assertThat(reservations.size()).isEqualTo(2);
    }

    @Test
    void 지정한_id를_가진_시간을_삭제할_수_있다() {
        Reservation reservation = reservationService.create(new ReservationRequestDto(
                "유저1", LocalDate.of(2026, 5, 3), 1L));
        Long id = 1L;

        reservationService.delete(id);

        List<Reservation> reservations = reservationService.findAll();
        assertThat(reservations.size()).isEqualTo(0);
    }

    @Test
    void 삭제하려는_id가_존재하지_않으면_예외_처리한다() {
        Long id = 1L;

        assertThatThrownBy(() -> {
            reservationService.delete(id);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}