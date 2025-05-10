package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
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
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.impl.ReservationRepositoryImpl;

public class ReservationRepositoryTest {

    private ReservationRepository reservationRepository;
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
        reservationRepository = new ReservationRepositoryImpl(reservationDao);
    }

    @Test
    @DisplayName("존재하지 않는 예약ID를 가져오려고 할 경우, 예외가 발생해야 한다.")
    void not_exist_reservation_id_get_then_throw_exception() {
        assertThatThrownBy(() -> reservationRepository.findById(999L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하는 예약ID를 가져오려고 할 경우, 성공해야 한다")
    void exist_reservation_id_get_then_success() {
        Reservation reservation = createReservation(createMember(), createTheme(),
            createReservationTime());
        long savedId = reservationDao.save(reservation);
        reservation.setId(savedId);
        assertThatCode(() -> reservationRepository.findById(savedId))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 예약ID를 삭제하려고 할 경우, 예외가 발생해야 한다.")
    void not_exist_theme_id_delete_then_throw_exception() {
        Reservation reservation = createReservation(createMember(), createTheme(),
            createReservationTime());
        long savedId = reservationDao.save(reservation);
        reservation.setId(savedId);
        assertThatCode(() -> reservationRepository.delete(savedId))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이미 날짜+시간에 예약이 존재한다면, true를 반환해야 한다.")
    void already_another_reservation_from_date_time_then_return_true() {
        //given
        Theme theme = createTheme();
        ReservationTime reservationTime = createReservationTime();
        Member member = createMember();
        Reservation reservation = createReservation(member, theme, reservationTime);
        long savedId = reservationDao.save(reservation);
        reservation.setId(savedId);

        //when
        Reservation anotherReservation = createReservation(member, theme, reservationTime);
        boolean result = reservationRepository.hasAnotherReservation(
            anotherReservation.getReservationDate(),
            anotherReservation.getTimeId());

        //then
        assertThat(result).isTrue();
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
