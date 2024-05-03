package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.response.MemberReservationTimeResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.DuplicatedException;
import roomescape.exception.NotFoundException;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.dao.ReservationTimeDao;
import roomescape.service.dto.ReservationDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ReservationServiceTest {

    private final ReservationTimeDao reservationTimeDao = new FakeReservationTimeDao();
    private final ReservationRepository reservationRepository = new ReservationRepository(
            new FakeReservationDao(), reservationTimeDao, new FakeThemeDao());
//    private ReservationTimeRepository reservationTimeRepository = new ReservationTimeRepository(
//            new FakeReservationDao(), new FakeReservationTimeDao());
    private final ReservationService reservationService = new ReservationService(reservationRepository);


    @DisplayName("모든 예약 시간을 반환한다")
    @Test
    void should_return_all_reservation_times() {
        List<Reservation> reservations = reservationService.findAllReservations();
        assertThat(reservations).hasSize(2);
    }

    @DisplayName("예약 시간을 추가한다")
    @Test
    void should_add_reservation_times() {
        reservationService.saveReservation(
                new ReservationDto("네오", LocalDate.of(2030, 1, 1), 1L, 1L));
        List<Reservation> allReservations = reservationService.findAllReservations();
        assertThat(allReservations).hasSize(3);
    }

    @DisplayName("예약 시간을 삭제한다")
    @Test
    void should_remove_reservation_times() {
        reservationService.deleteReservation(1);
        List<Reservation> allReservations = reservationService.findAllReservations();
        assertThat(allReservations).hasSize(1);
    }

    @DisplayName("존재하지 않는 예약을 삭제하면 예외가 발생한다.")
    @Test
    void should_throw_exception_when_not_exist_reservation_time() {
        assertThatThrownBy(() -> reservationService.deleteReservation(100000000))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] 존재하지 않는 예약입니다.");
    }

    @DisplayName("존재하는 예약을 삭제하면 예외가 발생하지 않는다.")
    @Test
    void should_not_throw_exception_when_exist_reservation_time() {
        assertThatCode(() -> reservationService.deleteReservation(1))
                .doesNotThrowAnyException();
    }

    @DisplayName("현재 이전으로 예약하면 예외가 발생한다.")
    @Test
    void should_throw_exception_when_previous_date() {
        ReservationDto request = new ReservationDto("에버", LocalDate.of(2000, 1, 11), 1L, 1L);
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 현재 이전 예약은 할 수 없습니다.");
    }

    @DisplayName("현재로 예약하면 예외가 발생하지 않는다.")
    @Test
    void should_not_throw_exception_when_current_date() {
        reservationTimeDao.saveReservationTime(new ReservationTime(3, LocalTime.now()));
        ReservationDto request = new ReservationDto("에버", LocalDate.now(), 3L, 1L);
        assertThatCode(() -> reservationService.saveReservation(request))
                .doesNotThrowAnyException();
    }

    @DisplayName("현재 이후로 예약하면 예외가 발생하지 않는다.")
    @Test
    void should_not_throw_exception_when_later_date() {
        ReservationDto request = new ReservationDto("에버", LocalDate.of(2030, 1, 11), 1L, 1L);
        assertThatCode(() -> reservationService.saveReservation(request))
                .doesNotThrowAnyException();
    }

    @DisplayName("날짜, 시간이 일치하는 예약을 추가하려 할 때 예외가 발생한다.")
    @Test
    void should_throw_exception_when_add_exist_reservation() {
        ReservationDto request = new ReservationDto("배키", LocalDate.of(2030, 8, 5), 2L, 2L);
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(DuplicatedException.class)
                .hasMessage("[ERROR] 중복되는 예약은 추가할 수 없습니다.");
    }

    @DisplayName("예약 가능 상태를 담은 시간 정보를 반환한다.")
    @Test
    void should_return_times_with_book_state() {
        List<MemberReservationTimeResponse> times = reservationService.findReservationTimesInformation(LocalDate.of(2030, 8, 5), 1);
        assertThat(times).hasSize(2);
        assertThat(times).containsOnly(
                new MemberReservationTimeResponse(1, LocalTime.of(10, 0), false),
                new MemberReservationTimeResponse(2, LocalTime.of(11, 0), true)
        );
    }
}
