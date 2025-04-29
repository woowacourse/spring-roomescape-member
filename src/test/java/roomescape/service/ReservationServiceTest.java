package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.fake.FakeReservationDao;
import roomescape.fake.FakeReservationTimeDao;
import roomescape.service.param.CreateReservationParam;
import roomescape.service.result.ReservationResult;
import roomescape.service.result.ReservationTimeResult;

class ReservationServiceTest {

    @Test
    void 예약을_생성한다() {
        //given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());
        FakeReservationTimeDao fakeReservationTimeDao = new FakeReservationTimeDao(reservationTime);
        FakeReservationDao fakeReservationDao = new FakeReservationDao();
        ReservationService reservationService = new ReservationService(fakeReservationTimeDao, fakeReservationDao);
        CreateReservationParam createReservationParam = new CreateReservationParam("test", LocalDate.of(2025, 4, 25),
                1L);

        //when
        Long createdId = reservationService.create(createReservationParam);

        //then
        assertThat(fakeReservationDao.findById(createdId))
                .hasValue(new Reservation(1L, "test", LocalDate.of(2025, 4, 25), reservationTime));
    }

    @Test
    void 예약을_생성할때_timeId가_데이터베이스에_존재하지_않는다면_예외가_발생한다() {
        //give
        FakeReservationTimeDao fakeReservationTimeDao = new FakeReservationTimeDao();
        FakeReservationDao fakeReservationDao = new FakeReservationDao();
        ReservationService reservationService = new ReservationService(fakeReservationTimeDao, fakeReservationDao);
        CreateReservationParam createReservationParam = new CreateReservationParam("test", LocalDate.of(2025, 4, 25),
                1L);

        //when & then
        assertThatThrownBy(() -> reservationService.create(createReservationParam))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("1에 해당하는 reservation_time 튜플이 없습니다.");
    }

    @Test
    void id값으로_예약을_삭제할_수_있다() {
        //given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());
        Reservation reservation = new Reservation(1L, "test", LocalDate.now(), reservationTime);
        FakeReservationTimeDao fakeReservationTimeDao = new FakeReservationTimeDao(reservationTime);
        FakeReservationDao fakeReservationDao = new FakeReservationDao(reservation);
        ReservationService reservationService = new ReservationService(fakeReservationTimeDao, fakeReservationDao);

        //when
        reservationService.deleteById(1L);

        //then
        assertThat(fakeReservationDao.findById(1L)).isEmpty();
    }

    @Test
    void 전체_예약을_조회할_수_있다() {
        //given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(11, 1));
        FakeReservationTimeDao fakeReservationTimeDao = new FakeReservationTimeDao(reservationTime);
        FakeReservationDao fakeReservationDao = new FakeReservationDao(
                new Reservation(1L, "test1", LocalDate.of(2025, 4, 26), reservationTime),
                new Reservation(2L, "test2", LocalDate.of(2025, 4, 27), reservationTime)
        );
        ReservationService reservationService = new ReservationService(fakeReservationTimeDao, fakeReservationDao);

        //when
        List<ReservationResult> reservationResults = reservationService.findAll();

        //then
        assertThat(reservationResults).isEqualTo(List.of(
                new ReservationResult(1L, "test1", LocalDate.of(2025, 4, 26),
                        new ReservationTimeResult(1L, LocalTime.of(11, 1))),
                new ReservationResult(2L, "test2", LocalDate.of(2025, 4, 27),
                        new ReservationTimeResult(1L, LocalTime.of(11, 1)))
        ));
    }

    @Test
    void id_로_예약을_찾을_수_있다() {
        //given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(11, 1));
        FakeReservationTimeDao fakeReservationTimeDao = new FakeReservationTimeDao(reservationTime);
        FakeReservationDao fakeReservationDao = new FakeReservationDao(
                new Reservation(1L, "test1", LocalDate.of(2025, 4, 26), reservationTime));
        ReservationService reservationService = new ReservationService(fakeReservationTimeDao, fakeReservationDao);

        //when
        ReservationResult reservationResult = reservationService.findById(1L);

        //then
        assertThat(reservationResult).isEqualTo(
                new ReservationResult(1L, "test1", LocalDate.of(2025, 4, 26),
                        new ReservationTimeResult(1L, LocalTime.of(11, 1)))
        );
    }

    @Test
    void id에_해당하는_예약이_없는경우_예외가_발생한다() {
        //given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(11, 1));
        FakeReservationTimeDao fakeReservationTimeDao = new FakeReservationTimeDao(reservationTime);
        FakeReservationDao fakeReservationDao = new FakeReservationDao(
                new Reservation(1L, "test1", LocalDate.of(2025, 4, 26), reservationTime));
        ReservationService reservationService = new ReservationService(fakeReservationTimeDao, fakeReservationDao);

        //when & then
        assertThatThrownBy(() -> reservationService.findById(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("2에 해당하는 reservation_time 튜플이 없습니다.");
    }
}