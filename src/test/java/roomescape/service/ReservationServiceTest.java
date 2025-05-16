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
import roomescape.domain.reservation.dto.request.ReservationRequestDto;
import roomescape.domain.reservation.model.Reservation;
import roomescape.domain.reservation.model.ReservationDate;
import roomescape.domain.reservation.service.ReservationService;
import roomescape.domain.reservationtime.dao.ReservationTimeDao;
import roomescape.domain.reservationtime.model.ReservationTime;
import roomescape.domain.theme.dao.ThemeDao;
import roomescape.domain.theme.model.Theme;
import roomescape.global.exception.DuplicateException;
import roomescape.global.exception.NotFoundException;

public class ReservationServiceTest {

    private ReservationService reservationService;
    private MemberDao memberDao;
    private ReservationTimeDao reservationTimeDao;
    private ThemeDao themeDao;
    private ReservationDao reservationDao;

    @BeforeEach
    void init() {
        reservationDao = new FakeReservationDaoImpl();
        memberDao = new FakeMemberDaoImpl();
        themeDao = new FakeThemeDaoImpl();
        reservationTimeDao = new FakeReservationTimeDaoImpl();
        reservationService = new ReservationService(
            reservationDao, reservationTimeDao, themeDao, new TestTime());
    }

    @Test
    @DisplayName("존재하지 않는 예약ID를 가져오려고 할 경우, 예외가 발생해야 한다.")
    void not_exist_reservation_id_get_then_throw_exception() {
        assertThatThrownBy(() -> reservationService.findReservationBy(999L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("존재하는 예약ID를 가져오려고 할 경우, 성공해야 한다")
    void exist_reservation_id_get_then_success() {
        Reservation reservation = createReservation(createMember(), createTheme(),
            createReservationTime());
        long savedId = reservationDao.save(reservation);
        reservation.setId(savedId);
        assertThatCode(() -> reservationService.findReservationBy(savedId))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하는 예약ID를 삭제하려고 할 경우, 성공해야 한다.")
    void not_exist_theme_id_delete_then_throw_exception() {
        Reservation reservation = createReservation(createMember(), createTheme(),
            createReservationTime());
        long savedId = reservationDao.save(reservation);
        reservation.setId(savedId);
        assertThatCode(() -> reservationService.deleteReservation(savedId))
            .doesNotThrowAnyException();
    }

    @DisplayName("이미 존재하는 날짜와 시간에 예약하려고 하면, 예외가 발생해야 한다.")
    @Test
    void already_exist_date_time_save_reservation_then_throw_exception() {
        //given
        Member member = Member.createUser("jenson", "b@naver.com", "abc");
        ReservationDate reservationDate = new ReservationDate(LocalDate.of(2025, 5, 5));
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포", "공포테마입니다", "http://aaa");

        long savedMemberId = memberDao.save(member);
        member.setId(savedMemberId);

        long savedThemeId = themeDao.save(theme);
        theme.setId(savedThemeId);

        long savedReservationTimeId = reservationTimeDao.save(reservationTime);
        reservationTime.setId(savedReservationTimeId);

        Reservation reservation = new Reservation(
            member,
            reservationDate,
            reservationTime,
            theme);
        reservationDao.save(reservation);

        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(
            "2025-05-05",
            savedThemeId,
            savedThemeId
        );

        //when, then
        assertThatThrownBy(
            () -> reservationService.saveReservationOfMember(reservationRequestDto, member))
            .isInstanceOf(DuplicateException.class);
    }

    private Reservation createReservation(Member member, Theme theme,
        ReservationTime reservationTime) {
        return new Reservation(
            member,
            new ReservationDate(LocalDate.of(2025, 5, 5)),
            reservationTime,
            theme
        );
    }

    private Member createMember() {
        Member member = Member.createUser("testMember", "a@email.com", "aaa");
        long savedId = memberDao.save(member);
        member.setId(savedId);
        return member;
    }

    private Theme createTheme() {
        Theme theme = new Theme("공포", "공포테마입니다", "http://aaa");
        long savedThemeId = themeDao.save(theme);
        theme.setId(savedThemeId);
        return theme;
    }

    private ReservationTime createReservationTime() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        long savedReservationTimeId = reservationTimeDao.save(reservationTime);
        reservationTime.setId(savedReservationTimeId);
        return reservationTime;
    }
}
