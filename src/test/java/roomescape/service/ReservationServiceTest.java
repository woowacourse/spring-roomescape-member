package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.reservation.ReservationDao;
import roomescape.dao.resetvationTime.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.ReservationDuplicateException;

class ReservationServiceTest {

    List<Reservation> reservations = new ArrayList<>();
    List<ReservationTime> times = new ArrayList<>();

    ReservationTimeDao reservationTimeDao = new InMemoryReservationTimeDao(times);
    ReservationDao reservationDao = new InMemoryReservationDao(reservations, reservationTimeDao);
    ReservationTimeService reservationTimeService = new ReservationTimeService(reservationTimeDao);
    ReservationService reservationService = new ReservationService(reservationDao, reservationTimeService);

    @DisplayName("예약을 생성한다.")
    @Test
    void createTest() {

        // given
        ReservationTime savedReservationTime = reservationTimeDao.create(
                new ReservationTime(LocalTime.now().plusHours(1)));
        reservationService.create(
                new ReservationCreateRequest("체체", LocalDate.now().toString(), savedReservationTime.getId()));

        // when
        List<ReservationResponse> reservations = reservationService.findAll();

        // then
        assertThat(reservations.getFirst().name()).isEqualTo("체체");
    }

    @DisplayName("날짜와 시간이 중복되는 예약을 생성한다면 예외가 발생한다.")
    @Test
    void createThrowExceptionIfAlreadyExistDateAndTimeTest() {

        // given
        ReservationTime savedReservationTime = reservationTimeDao.create(
                new ReservationTime(LocalTime.now().plusHours(1)));
        reservationService.create(
                new ReservationCreateRequest("체체", LocalDate.now().toString(), savedReservationTime.getId()));

        // when & then
        assertThatThrownBy(() -> reservationService.create(
                new ReservationCreateRequest("체체", LocalDate.now().toString(), savedReservationTime.getId())))
                .isInstanceOf(ReservationDuplicateException.class)
                .hasMessage("이미 존재하는 예약입니다.");
    }

    @DisplayName("예약을 모두 찾는다.")
    @Test
    void findAllTest() {
        //TODO: 시간 빈으로 관리하기
        // given
        ReservationTime savedReservationTime1 = reservationTimeDao.create(
                new ReservationTime(LocalTime.now().plusHours(1)));
        ReservationTime savedReservationTime2 = reservationTimeDao.create(
                new ReservationTime(LocalTime.now().plusHours(2)));
        reservationService.create(
                new ReservationCreateRequest("체체", LocalDate.now().plusDays(1).toString(),
                        savedReservationTime1.getId()));
        reservationService.create(
                new ReservationCreateRequest("체체2", LocalDate.now().plusDays(1).toString(),
                        savedReservationTime2.getId()));

        // when
        List<ReservationResponse> reservations = reservationService.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(2);
    }

    @DisplayName("예약을 삭제한다.")
    @Test
    void deleteTest() {

        // given
        ReservationTime savedReservationTime = reservationTimeDao.create(
                new ReservationTime(LocalTime.now().plusHours(1)));
        reservationService.create(
                new ReservationCreateRequest("체체", LocalDate.now().toString(), savedReservationTime.getId()));

        // when
        reservationService.delete(savedReservationTime.getId());
        List<ReservationResponse> reservations = reservationService.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(0);
    }
}
