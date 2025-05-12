package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
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
import roomescape.domain.theme.dao.ThemeDao;
import roomescape.domain.theme.model.Theme;
import roomescape.domain.theme.service.ThemeService;
import roomescape.global.exception.InvalidReservationException;
import roomescape.global.exception.NotFoundException;

public class ThemeServiceTest {

    private final MemberDao memberDao = new FakeMemberDaoImpl();
    private final ThemeDao themeDao = new FakeThemeDaoImpl();
    private final ReservationTimeDao reservationTimeDao = new FakeReservationTimeDaoImpl();
    private final ReservationDao reservationDao = new FakeReservationDaoImpl();
    private final ThemeService themeService = new ThemeService(themeDao,
        reservationDao);

    @Test
    @DisplayName("존재하지 않는 테마ID를 가져오려고 할 경우, 예외가 발생해야 한다.")
    void not_exist_theme_id_get_then_throw_exception() {
        assertThatThrownBy(() -> themeService.findById(999L))
            .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("이미 테마 아이디를 예약에서 사용한다면, 예외가 발생해야 한다")
    @Test
    void delete_already_use_other_theme_id_then_throw_exception() {
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
            () -> themeService.deleteTheme(savedThemeId))
            .isInstanceOf(InvalidReservationException.class);
    }

    @Test
    @DisplayName("존재하는 테마ID를 가져오려고 할 경우, 성공해야 한다")
    void exist_theme_id_get_then_success() {
        Theme theme = new Theme("공포", "공포테마입니다", "http://aaa");
        long savedId = themeDao.save(theme);
        theme.setId(savedId);
        assertThatCode(() -> themeService.findById(savedId)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 테마ID를 삭제하려고 할 경우, 예외가 발생해야 한다.")
    void not_exist_theme_id_delete_then_throw_exception() {
        Theme theme = new Theme("공포", "공포테마입니다", "http://aaa");
        long savedId = themeDao.save(theme);
        theme.setId(savedId);
        assertThatCode(() -> themeService.deleteTheme(savedId)).doesNotThrowAnyException();
    }
}
