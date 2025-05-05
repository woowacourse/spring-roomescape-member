package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.controller.dto.AvailableTimeResponse;
import roomescape.reservation.controller.dto.ReservationTimeRequest;
import roomescape.reservation.controller.dto.ReservationTimeResponse;
import roomescape.fake.FakeReservationDao;
import roomescape.fake.FakeReservationTimeDao;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservation.repository.ReservationTimeDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

class ReservationTimeServiceTest {

    ReservationTimeDao reservationTimeDao = new FakeReservationTimeDao();
    ReservationDao reservationDao = new FakeReservationDao();
    ReservationTimeService reservationTimeService = new ReservationTimeService(reservationTimeDao, reservationDao);

    @DisplayName("이미 존재하는 시간을 저장할 경우 예외가 발생한다")
    @Test
    void should_ThrowException_WhenCreateDuplicateTime() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(11, 0));
        reservationTimeService.createReservationTime(request);
        // when
        // then
        assertThatThrownBy(() -> reservationTimeService.createReservationTime(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 시간입니다.");
    }

    @DisplayName("예약 시간을 저장할 수 있다")
    @Test
    void create() {
        // given
        LocalTime time = LocalTime.of(11, 0);
        ReservationTimeRequest request = new ReservationTimeRequest(time);
        // when
        ReservationTimeResponse result = reservationTimeService.createReservationTime(request);
        // then
        ReservationTime savedTime = reservationTimeDao.findById(1L);
        assertAll(
                () -> assertThat(result.id()).isEqualTo(1L),
                () -> assertThat(result.startAt()).isEqualTo(time),
                () -> assertThat(savedTime.getId()).isEqualTo(1L),
                () -> assertThat(savedTime.getStartAt()).isEqualTo(time)
        );
    }

    @DisplayName("예약 시간 목록을 조회할 수 있다")
    @Test
    void findAll() {
        // given
        ReservationTimeRequest request1 = new ReservationTimeRequest(LocalTime.of(11, 0));
        ReservationTimeRequest request2 = new ReservationTimeRequest(LocalTime.of(12, 0));
        reservationTimeService.createReservationTime(request1);
        reservationTimeService.createReservationTime(request2);
        // when
        List<ReservationTimeResponse> result = reservationTimeService.getReservationTimes();
        // then
        assertThat(result).hasSize(2);
    }

    @DisplayName("예약 시간을 삭제할 수 있다")
    @Test
    void testDelete() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(11, 0));
        reservationTimeService.createReservationTime(request);
        // when
        reservationTimeService.deleteReservationTimeById(1L);
        // then
        assertThat(reservationTimeDao.isNotExistsById(1L)).isTrue();
    }

    @DisplayName("예약이 존재하는 시간은 삭제할 경우 예외가 발생한다")
    @Test
    void should_ThrowException_WhenDeleteTimeWithinReservation() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(11, 0));
        ReservationTimeResponse response = reservationTimeService.createReservationTime(request);
        ReservationTime time = new ReservationTime(response.id(), response.startAt());
        Theme theme = new Theme(1L, "우테코방탈출", "탈출탈출탈출", "abcdefg");
        reservationDao.save(new Reservation(null, "노랑", LocalDate.now().plusDays(1), time, theme));
        // when
        // then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTimeById(response.id()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약이 존재하는 시간은 삭제할 수 없습니다.");
    }

    @DisplayName("예약 가능 시간을 조회할 수 있다.")
    @Test
    void findAvailableTimes() {
        // given
        ReservationTime savedTime1 = reservationTimeDao.save(new ReservationTime(LocalTime.of(10, 0)));
        ReservationTime savedTime2 = reservationTimeDao.save(new ReservationTime(LocalTime.of(15, 0)));
        Theme theme = new Theme(1L, "우테코 탈출", "우테코 방탈출", "wwwwww");
        LocalDate date = LocalDate.of(2025, 5, 1);
        reservationDao.save(new Reservation(1L, "leo", date, savedTime1, theme));
        // when
        List<AvailableTimeResponse> result = reservationTimeService.findAvailableTimes(date, theme.getId());
        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result).contains(
                        new AvailableTimeResponse(savedTime1.getId(), savedTime1.getStartAt(), true),
                        new AvailableTimeResponse(savedTime2.getId(), savedTime2.getStartAt(), false)
                )
        );
    }
}
