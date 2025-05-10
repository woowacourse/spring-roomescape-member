package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import roomescape.application.member.MemberResult;
import roomescape.application.reservation.dto.CreateReservationParam;
import roomescape.application.reservation.dto.ReservationResult;
import roomescape.application.reservation.dto.ReservationTimeResult;
import roomescape.application.reservation.dto.ThemeResult;
import roomescape.application.support.exception.NotFoundEntityException;
import roomescape.domain.BusinessRuleViolationException;
import roomescape.domain.member.Email;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;

class ReservationServiceTest extends ServiceIntegrationTest {

    public static final LocalDate RESERVATION_DATE = LocalDate.now().plusDays(1);

    @Test
    void 예약을_생성한다() {
        //given
        insertMember("test1", "email1@gmail.com", "password");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        reservationTimeRepository.create(reservationTime);
        themeRepository.create(theme);
        CreateReservationParam createReservationParam = new CreateReservationParam(RESERVATION_DATE, 1L, 1L, 1L);

        //when
        Long createdId = reservationService.create(createReservationParam);

        //then
        assertThat(reservationRepository.findById(createdId))
                .hasValue(new Reservation(1L,
                        new Member(1L, "test1", new Email("email1@gmail.com"), "password", Role.NORMAL),
                        RESERVATION_DATE,
                        reservationTime, theme));
    }

    @Test
    void 예약을_생성할때_timeId가_데이터베이스에_존재하지_않는다면_예외가_발생한다() {
        //give
        insertMember("test1", "email1@gmail.com", "password");
        themeRepository.create(new Theme(1L, "test", "description", "thumbnail"));
        CreateReservationParam createReservationParam = new CreateReservationParam(RESERVATION_DATE, 1L, 1L, 1L);

        //when & then
        assertThatThrownBy(() -> reservationService.create(createReservationParam))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage("1에 해당하는 reservation_time 튜플이 없습니다.");
    }

    @Test
    void id값으로_예약을_삭제할_수_있다() {
        //given
        insertMember("test1", "email1@gmail.com", "password");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        reservationTimeRepository.create(reservationTime);
        themeRepository.create(theme);
        reservationRepository.create(
                new Reservation(1L, new Member(1L, "test1", new Email("email1@gmail.com"), "password", Role.NORMAL),
                        RESERVATION_DATE,
                        reservationTime,
                        theme));

        //when
        reservationService.deleteById(1L);

        //then
        assertThat(reservationRepository.findById(1L)).isEmpty();
    }

    @Test
    void 전체_예약을_조회할_수_있다() {
        //given
        insertMember("test1", "email1@gmail.com", "password");
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(12, 1));
        ReservationTime reservationTime2 = new ReservationTime(2L, LocalTime.of(13, 1));
        themeRepository.create(theme);
        reservationTimeRepository.create(reservationTime1);
        reservationTimeRepository.create(reservationTime2);
        reservationRepository.create(
                new Reservation(1L, new Member(1L, "test1", new Email("email1@gmail.com"), "password", Role.NORMAL),
                        RESERVATION_DATE,
                        reservationTime1,
                        theme));
        reservationRepository.create(
                new Reservation(2L, new Member(1L, "test1", new Email("email1@gmail.com"), "password", Role.NORMAL),
                        RESERVATION_DATE,
                        reservationTime2,
                        theme));

        //when
        List<ReservationResult> reservationResults = reservationService.findAll();

        //then
        assertThat(reservationResults).isEqualTo(List.of(
                new ReservationResult(1L, new MemberResult(1L, "test1"), RESERVATION_DATE,
                        new ReservationTimeResult(1L, LocalTime.of(12, 1)),
                        new ThemeResult(1L, "test", "description", "thumbnail")),
                new ReservationResult(2L, new MemberResult(1L, "test1"), RESERVATION_DATE,
                        new ReservationTimeResult(2L, LocalTime.of(13, 1)),
                        new ThemeResult(1L, "test", "description", "thumbnail"))
        ));
    }

    @Test
    void id_로_예약을_찾을_수_있다() {
        //given
        insertMember("test1", "email1@gmail.com", "password");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        reservationTimeRepository.create(reservationTime);
        themeRepository.create(theme);
        reservationRepository.create(
                new Reservation(1L, new Member(1L, "test1", new Email("email1@gmail.com"), "password", Role.NORMAL),
                        RESERVATION_DATE,
                        reservationTime,
                        theme));

        //when
        ReservationResult reservationResult = reservationService.findById(1L);

        //then
        assertThat(reservationResult).isEqualTo(
                new ReservationResult(1L, new MemberResult(1L, "test1"), RESERVATION_DATE,
                        new ReservationTimeResult(1L, LocalTime.of(12, 0)),
                        new ThemeResult(1L, "test", "description", "thumbnail"))
        );
    }

    @Test
    void id에_해당하는_예약이_없는경우_예외가_발생한다() {
        //given
        insertMember("test1", "email1@gmail.com", "password");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        reservationTimeRepository.create(reservationTime);
        themeRepository.create(theme);
        reservationRepository.create(
                new Reservation(1L, new Member(1L, "test1", new Email("email1@gmail.com"), "password", Role.NORMAL),
                        RESERVATION_DATE,
                        reservationTime,
                        theme));

        //when & then
        assertThatThrownBy(() -> reservationService.findById(2L))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage("2에 해당하는 reservation 튜플이 없습니다.");
    }

    @Test
    void 날짜와_시간이_중복된_예약이_있으면_예외가_발생한다() {
        //given
        insertMember("test1", "email1@gmail.com", "password");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        reservationTimeRepository.create(reservationTime);
        themeRepository.create(theme);
        reservationRepository.create(
                new Reservation(1L, new Member(1L, "test1", new Email("email1@gmail.com"), "password", Role.NORMAL),
                        RESERVATION_DATE,
                        reservationTime,
                        theme));

        //when & then
        assertThatThrownBy(
                () -> reservationService.create(new CreateReservationParam(RESERVATION_DATE, 1L, 1L, 1L)))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("날짜와 시간이 중복된 예약이 존재합니다.");
    }
}