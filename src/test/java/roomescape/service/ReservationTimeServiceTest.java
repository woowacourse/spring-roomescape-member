package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.fake.FakeReservationTimeDao;
import roomescape.service.param.CreateReservationTimeParam;
import roomescape.service.result.ReservationTimeResult;

class ReservationTimeServiceTest {

    @Test
    void 예약_시간을_생성할_수_있다() {
        //given
        FakeReservationTimeDao fakeReservationTimeDao = new FakeReservationTimeDao();
        ReservationTimeService reservationTimeService = new ReservationTimeService(fakeReservationTimeDao);

        //when
        Long createdId = reservationTimeService.create(new CreateReservationTimeParam(LocalTime.of(11, 1)));

        //then
        assertThat(fakeReservationTimeDao.findById(createdId))
                .hasValue(new ReservationTime(1L, LocalTime.of(11, 1)));
    }

    @Test
    void id에_해당하는_예약_시간을_찾을_수_있다() {
        //given
        FakeReservationTimeDao fakeReservationTimeDao = new FakeReservationTimeDao(
                new ReservationTime(1L, LocalTime.of(1, 1)));
        ReservationTimeService reservationTimeService = new ReservationTimeService(fakeReservationTimeDao);

        //when
        ReservationTimeResult reservationTimeResult = reservationTimeService.findById(1L);

        //then
        assertThat(reservationTimeResult).isEqualTo(new ReservationTimeResult(1L, LocalTime.of(1, 1)));
    }

    @Test
    void id에_해당하는_예약_시간이_없는경우_예외가_발생한다() {
        //given
        FakeReservationTimeDao fakeReservationTimeDao = new FakeReservationTimeDao();
        ReservationTimeService reservationTimeService = new ReservationTimeService(fakeReservationTimeDao);

        //when & then
        assertThatThrownBy(() -> reservationTimeService.findById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("1에 해당하는 reservation_time 튜플이 없습니다.");
    }

    @Test
    void 전체_예약_시간을_조회할_수_있다() {
        //given
        FakeReservationTimeDao fakeReservationTimeDao = new FakeReservationTimeDao(
                new ReservationTime(1L, LocalTime.of(1, 1)));
        ReservationTimeService reservationTimeService = new ReservationTimeService(fakeReservationTimeDao);

        //when
        List<ReservationTimeResult> reservationTimeResults = reservationTimeService.findAll();

        //then
        assertThat(reservationTimeResults).isEqualTo(List.of(
                new ReservationTimeResult(1L, LocalTime.of(1, 1))
        ));
    }

    @Test
    void id에_해당하는_예약_시간을_삭제한다() {
        //given
        FakeReservationTimeDao fakeReservationTimeDao = new FakeReservationTimeDao(
                new ReservationTime(1L, LocalTime.of(1, 1)));
        ReservationTimeService reservationTimeService = new ReservationTimeService(fakeReservationTimeDao);

        //when
        reservationTimeService.deleteById(1L);

        //then
        assertThat(fakeReservationTimeDao.findById(1L)).isEmpty();
    }
}