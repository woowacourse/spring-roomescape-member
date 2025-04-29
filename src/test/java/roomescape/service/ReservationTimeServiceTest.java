package roomescape.service;

import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.service.param.CreateReservationTimeParam;
import roomescape.service.result.ReservationTimeResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeServiceTest {

    @Test
    void 예약_시간을_생성할_수_있다() {
        //given
        FakeReservationRepository fakeReservationDao = new FakeReservationRepository();
        FakeReservationTimeRepository fakeReservationTimeDao = new FakeReservationTimeRepository();
        ReservationTimeService reservationTimeService = new ReservationTimeService(fakeReservationTimeDao, fakeReservationDao);

        //when
        Long createdId = reservationTimeService.create(new CreateReservationTimeParam(LocalTime.of(12, 1)));

        //then
        assertThat(fakeReservationTimeDao.findById(createdId))
                .hasValue(new ReservationTime(1L, LocalTime.of(12, 1)));
    }

    @Test
    void id에_해당하는_예약_시간을_찾을_수_있다() {
        //given
        FakeReservationRepository fakeReservationDao = new FakeReservationRepository();
        FakeReservationTimeRepository fakeReservationTimeDao = new FakeReservationTimeRepository(
                new ReservationTime(1L, LocalTime.of(12, 1)));
        ReservationTimeService reservationTimeService = new ReservationTimeService(fakeReservationTimeDao, fakeReservationDao);

        //when
        ReservationTimeResult reservationTimeResult = reservationTimeService.findById(1L);

        //then
        assertThat(reservationTimeResult).isEqualTo(new ReservationTimeResult(1L, LocalTime.of(12, 1)));
    }

    @Test
    void id에_해당하는_예약_시간이_없는경우_예외가_발생한다() {
        //given
        FakeReservationRepository fakeReservationDao = new FakeReservationRepository();
        FakeReservationTimeRepository fakeReservationTimeDao = new FakeReservationTimeRepository();
        ReservationTimeService reservationTimeService = new ReservationTimeService(fakeReservationTimeDao, fakeReservationDao);

        //when & then
        assertThatThrownBy(() -> reservationTimeService.findById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("1에 해당하는 reservation_time 튜플이 없습니다.");
    }

    @Test
    void 전체_예약_시간을_조회할_수_있다() {
        //given
        FakeReservationRepository fakeReservationDao = new FakeReservationRepository();
        FakeReservationTimeRepository fakeReservationTimeDao = new FakeReservationTimeRepository(
                new ReservationTime(1L, LocalTime.of(12, 1)));
        ReservationTimeService reservationTimeService = new ReservationTimeService(fakeReservationTimeDao, fakeReservationDao);

        //when
        List<ReservationTimeResult> reservationTimeResults = reservationTimeService.findAll();

        //then
        assertThat(reservationTimeResults).isEqualTo(List.of(
                new ReservationTimeResult(1L, LocalTime.of(12, 1))
        ));
    }

    @Test
    void id에_해당하는_예약_시간을_삭제한다() {
        //given
        FakeReservationRepository fakeReservationDao = new FakeReservationRepository();
        FakeReservationTimeRepository fakeReservationTimeDao = new FakeReservationTimeRepository(
                new ReservationTime(1L, LocalTime.of(12, 1)));
        ReservationTimeService reservationTimeService = new ReservationTimeService(fakeReservationTimeDao, fakeReservationDao);

        //when
        reservationTimeService.deleteById(1L);

        //then
        assertThat(fakeReservationTimeDao.findById(1L)).isEmpty();
    }

    @Test
    void time_id를_사용하는_예약이_존재하면_예외를_던진다() {
        //given
        FakeReservationTimeRepository fakeReservationTimeDao = new FakeReservationTimeRepository(
                new ReservationTime(1L, LocalTime.of(12, 1)));
        FakeReservationRepository fakeReservationDao = new FakeReservationRepository(new Reservation(1L, "test1", LocalDate.of(2025, 4, 30), new ReservationTime(1L, LocalTime.of(12, 1))));
        ReservationTimeService reservationTimeService = new ReservationTimeService(fakeReservationTimeDao, fakeReservationDao);

        //when, then
        assertThatThrownBy(() -> reservationTimeService.deleteById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 예약 시간에 예약이 존재합니다.");
    }
}