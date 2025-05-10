package roomescape.service;


import static org.assertj.core.api.Assertions.assertThat;

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
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequestDto;

public class ReservationServiceTest {

    private static final LocalDate NOW_DATE = LocalDate.now().plusDays(1);

    private ReservationService reservationService;
    private ReservationDao reservationDao;
    private ReservationTimeDao reservationTimeDao;
    private ThemeDao themeDao;
    private MemberDao memberDao;

    @BeforeEach
    void init() {
        reservationDao = new FakeReservationDaoImpl();
        reservationTimeDao = new FakeReservationTimeDaoImpl();
        themeDao = new FakeThemeDaoImpl();
        memberDao = new FakeMemberDaoImpl();
        reservationService = new ReservationService(reservationDao, reservationTimeDao, themeDao, memberDao);
    }

    @DisplayName("ReservationRequestDto가 주어졌을 때, Fake 객체에 정상적으로 저장되어야 한다.")
    @Test
    void given_reservation_request_dto_then_save() {
        //given
        LoginMember loginMember = new LoginMember(1L, "testName", "testEmail", "basic");

        memberDao.save(Member.from(1L, "testName", "testEmail", "1234"));
        reservationTimeDao.saveReservationTime(new ReservationTime(1L, LocalTime.of(10, 30)));
        themeDao.saveTheme(new Theme(1L, "name", "description", "thumbnail"));
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(
                NOW_DATE.toString(), 1L, 1L);

        //when
        reservationService.saveReservation(reservationRequestDto, loginMember);

        //then
        assertThat(reservationService.getAllReservations().size()).isEqualTo(1);
    }

    @DisplayName("여러 번 Reservation을 저장할 때, 성공적으로 Fake객체에 저장되고, 읽어올 수 있어야 한다.")
    @Test
    void given_multiple_reservation_request_dto_then_all_save() {

        memberDao.save(Member.from(1L, "testName1", "testEmail1", "12"));
        memberDao.save(Member.from(2L, "testName2", "testEmail2", "123"));
        memberDao.save(Member.from(3L, "testName3", "testEmail3", "1234"));

        reservationTimeDao.saveReservationTime(new ReservationTime(1L, LocalTime.of(10, 30)));
        reservationTimeDao.saveReservationTime(new ReservationTime(2L, LocalTime.of(11, 30)));
        reservationTimeDao.saveReservationTime(new ReservationTime(3L, LocalTime.of(12, 30)));

        themeDao.saveTheme(new Theme(1L, "name1", "description1", "thumbnail1"));
        themeDao.saveTheme(new Theme(2L, "name2", "description2", "thumbnail2"));
        themeDao.saveTheme(new Theme(3L, "name3", "description3", "thumbnail3"));

        LoginMember loginMember1 = new LoginMember(1L, "testName1", "testEmail1", "basic");
        LoginMember loginMember2 = new LoginMember(2L, "testName2", "testEmail2", "basic");
        LoginMember loginMember3 = new LoginMember(3L, "testName3", "testEmail3", "basic");

        //given
        ReservationRequestDto reservationRequestDto1 = new ReservationRequestDto(
                NOW_DATE.toString(), 1L, 1L);
        ReservationRequestDto reservationRequestDto2 = new ReservationRequestDto(
                NOW_DATE.plusDays(1).toString(), 2L, 2L);
        ReservationRequestDto reservationRequestDto3 = new ReservationRequestDto(
                NOW_DATE.plusDays(2).toString(), 3L, 3L);

        //when
        reservationService.saveReservation(reservationRequestDto1, loginMember1);
        reservationService.saveReservation(reservationRequestDto2, loginMember2);
        reservationService.saveReservation(reservationRequestDto3, loginMember3);

        //then
        assertThat(reservationService.getAllReservations().size()).isEqualTo(3);
    }

    @DisplayName("reservationId가 주어졌을 떄, Fake 객체에서 삭제되어야 한다.")
    @Test
    void given_reservation_id_then_remove_db() {
        //given
        LoginMember loginMember = new LoginMember(1L, "testName", "testEmail", "basic");
        memberDao.save(Member.from(1L, "testName", "testEmail", "1234"));
        reservationTimeDao.saveReservationTime(new ReservationTime(1L, LocalTime.of(10, 30)));
        themeDao.saveTheme(new Theme(1L, "name", "description", "thumbnail"));

        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(NOW_DATE.toString(), 1L, 1L);
        reservationService.saveReservation(reservationRequestDto, loginMember);
        //when
        reservationService.deleteReservation(1L);

        //then
        assertThat(reservationService.getAllReservations().size()).isEqualTo(0);
    }

}
