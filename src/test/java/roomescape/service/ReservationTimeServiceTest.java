package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.FakeMemberDaoImpl;
import roomescape.dao.FakeReservationDaoImpl;
import roomescape.dao.FakeReservationTimeDaoImpl;
import roomescape.dao.FakeThemeDaoImpl;
import roomescape.domain.member.dao.MemberDao;
import roomescape.domain.member.model.Member;
import roomescape.domain.reservation.dao.ReservationDao;
import roomescape.domain.reservation.model.Reservation;
import roomescape.domain.reservation.model.ReservationDate;
import roomescape.domain.reservationtime.dao.ReservationTimeDao;
import roomescape.domain.reservationtime.model.ReservationTime;
import roomescape.domain.reservationtime.service.ReservationTimeService;
import roomescape.domain.theme.dao.ThemeDao;
import roomescape.domain.theme.model.Theme;
import roomescape.global.exception.InvalidReservationException;

public class ReservationTimeServiceTest {

    private MemberDao memberDao;
    private ReservationTimeDao reservationTimeDao;
    private ReservationDao reservationDao;
    private ThemeDao themeDao;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void init() {
        memberDao = new FakeMemberDaoImpl();
        reservationTimeDao = new FakeReservationTimeDaoImpl();
        reservationDao = new FakeReservationDaoImpl();
        themeDao = new FakeThemeDaoImpl();
        reservationTimeService = new ReservationTimeService(
            reservationTimeDao, reservationDao);
    }

    @DisplayName("존재하지 않는 예약 시간을 삭제하려고 할 경우, 예외가 발생해야 한다.")
    @Test
    void delete_not_exist_reservation_id_then_throw_exception() {
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(10000L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 예약 시간을 삭제하려고 할 경우, 성공해야 한다.")
    @Test
    void exist_reservation_id_delete_then_success() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        long savedReservationTimeId = reservationTimeDao.save(reservationTime);
        reservationTime.setId(savedReservationTimeId);
        assertThatCode(
            () -> reservationTimeService.deleteReservationTime(savedReservationTimeId))
            .doesNotThrowAnyException();
    }

    @DisplayName("다른 예약에서 사용중인 예약 시간을 삭제하려고 할 경우, 예외가 발생해야 한다.")
    @Test
    void delete_already_use_other_reservation_time_id_then_throw_exception() {

        //given
        Member member = Member.createUser("testMember", "a@email.com", "aaa");
        long savedMemberId = memberDao.save(member);
        member.setId(savedMemberId);

        Theme theme = new Theme("공포", "공포테마입니다", "http://aaa");
        long savedThemeId = themeDao.save(theme);
        theme.setId(savedThemeId);

        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        long savedReservationTimeId = reservationTimeDao.save(reservationTime);
        reservationTime.setId(savedReservationTimeId);

        Reservation reservation = new Reservation(
            member,
            new ReservationDate(LocalDate.of(2025, 5, 5)),
            reservationTime,
            theme
        );

        long savedId = reservationDao.save(reservation);
        reservation.setId(savedId);

        //when
        assertThatThrownBy(
            () -> reservationTimeService.deleteReservationTime(savedReservationTimeId))
            .isInstanceOf(InvalidReservationException.class);
    }
}
