package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.BusinessRuleViolationException;
import roomescape.common.exception.NotFoundEntityException;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.service.param.CreateReservationTimeParam;
import roomescape.service.result.AvailableReservationTimeResult;
import roomescape.service.result.ReservationTimeResult;

class ReservationTimeServiceTest extends ServiceIntegrationTest {

    @Test
    void 예약_시간을_생성할_수_있다() {
        //given & when
        Long createdId = reservationTimeService.create(new CreateReservationTimeParam(LocalTime.of(12, 1)));

        //then
        assertThat(reservationTimeRepository.findById(createdId))
                .hasValue(new ReservationTime(1L, LocalTime.of(12, 1)));
    }

    @Test
    void id에_해당하는_예약_시간을_찾을_수_있다() {
        //given
        reservationTimeRepository.create(new ReservationTime(1L, LocalTime.of(12, 1)));

        //when
        ReservationTimeResult reservationTimeResult = reservationTimeService.findById(1L);

        //then
        assertThat(reservationTimeResult).isEqualTo(new ReservationTimeResult(1L, LocalTime.of(12, 1)));
    }

    @Test
    void id에_해당하는_예약_시간이_없는경우_예외가_발생한다() {
        //given & when & then
        assertThatThrownBy(() -> reservationTimeService.findById(1L))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage("1에 해당하는 reservation_time 튜플이 없습니다.");
    }

    @Test
    void 전체_예약_시간을_조회할_수_있다() {
        //given
        reservationTimeRepository.create(new ReservationTime(1L, LocalTime.of(12, 1)));

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
        reservationTimeRepository.create(new ReservationTime(1L, LocalTime.of(12, 1)));

        //when
        reservationTimeService.deleteById(1L);

        //then
        assertThat(reservationTimeRepository.findById(1L)).isEmpty();
    }

    @Test
    void time_id를_사용하는_예약이_존재하면_예외를_던진다() {
        //given
        insertMember("test1", "email1", "password");
        themeRepository.create(new Theme(1L, "name", "description", "thumbnail"));
        reservationTimeRepository.create(new ReservationTime(1L, LocalTime.of(12, 1)));
        reservationRepository.create(
                new Reservation(1L, new Member(1L, "test1", "email1", "password", Role.NORMAL),
                        LocalDate.of(2025, 4, 30),
                        new ReservationTime(1L, LocalTime.of(12, 1)),
                        new Theme(1L, "name", "description", "thumbnail")));

        //when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(1L))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("해당 예약 시간에 예약이 존재합니다.");
    }

    @Test
    void 특정테마의_특정날짜에_예약된_시간_정보를_조회할_수_있다() {
        //given
        insertMember("test1", "email1", "password");
        insertMember("test2", "email2", "password");
        themeRepository.create(new Theme(1L, "name", "description", "thumbnail"));

        reservationTimeRepository.create(new ReservationTime(1L, LocalTime.of(12, 0)));
        reservationTimeRepository.create(new ReservationTime(1L, LocalTime.of(12, 10)));
        reservationTimeRepository.create(new ReservationTime(1L, LocalTime.of(12, 20)));

        insertReservation(1L, LocalDate.now(clock), 1L, 1L);
        insertReservation(2L, LocalDate.now(clock), 3L, 1L);

        //when
        List<AvailableReservationTimeResult> availableTimesByThemeIdAndDate = reservationTimeService.findAvailableTimesByThemeIdAndDate(
                1L, LocalDate.now(clock));

        //then
        assertThat(availableTimesByThemeIdAndDate).isEqualTo(List.of(
                new AvailableReservationTimeResult(1L, LocalTime.of(12, 0), true),
                new AvailableReservationTimeResult(2L, LocalTime.of(12, 10), false),
                new AvailableReservationTimeResult(3L, LocalTime.of(12, 20), true)
        ));
    }
}