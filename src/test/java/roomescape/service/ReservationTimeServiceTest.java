package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.dto.ReservationTimeRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.fake.FakeReservationDao;
import roomescape.fake.FakeReservationTimeDao;
import roomescape.service.reservation.Reservation;
import roomescape.service.reservation.ReservationTime;
import roomescape.service.reservation.Theme;

class ReservationTimeServiceTest {

    FakeReservationTimeDao reservationTimeDao = new FakeReservationTimeDao();
    FakeReservationDao reservationDao = new FakeReservationDao();
    ReservationTimeService reservationTimeService = new ReservationTimeService(reservationTimeDao, reservationDao);

    @DisplayName("이미 존재하는 시간을 저장할 경우 예외가 발생한다.")
    @Test
    void testValidateDuplication() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(11, 0));
        reservationTimeService.createReservationTime(request);
        // when
        // then
        assertThatThrownBy(() -> reservationTimeService.createReservationTime(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 시간입니다.");
    }

    @DisplayName("예약 시간을 저장할 수 있다.")
    @Test
    void testCreate() {
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

    @DisplayName("예약 시간 목록을 조회할 수 있다.")
    @Test
    void testFindAll() {
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

    @DisplayName("예약 시간을 삭제할 수 있다.")
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

    @DisplayName("예약이 존재하는 시간은 삭제할 경우 예외가 발생한다.")
    @Test
    void testIllegalDelete() {
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
}
