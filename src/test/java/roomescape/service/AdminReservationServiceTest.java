package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.dao.JdbcMemberDao;
import roomescape.dao.JdbcReservationDao;
import roomescape.dao.JdbcReservationTimeDao;
import roomescape.dao.JdbcThemeDao;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.dto.reservation.ReservationFilterRequest;
import roomescape.fixture.MemberFixtures;
import roomescape.fixture.ReservationFixtures;
import roomescape.fixture.ReservationTimeFixtures;
import roomescape.fixture.ThemeFixtures;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:test_db_clean.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class AdminReservationServiceTest {

    @Autowired
    private JdbcReservationDao reservationDao;
    @Autowired
    private JdbcReservationTimeDao reservationTimeDao;
    @Autowired
    private JdbcThemeDao themeDao;
    @Autowired
    private JdbcMemberDao memberDao;
    @Autowired
    private AdminReservationService adminReservationService;

    @Test
    @DisplayName("요청에 시작시간이 종료시간보다 이후이면 예외가 발생한다.")
    void findFilteredWhenFromDateAfterToDate() {
        //given
        Theme theme = themeDao.create(ThemeFixtures.createDefaultTheme());
        Member member = memberDao.create(MemberFixtures.createUserMember("daon"));
        ReservationTime time1 = reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:02"));
        ReservationTime time2 = reservationTimeDao.create(ReservationTimeFixtures.createReservationTime("12:20"));
        Reservation reservation1 = ReservationFixtures.createReservation(member, time1, theme);
        Reservation reservation2 = ReservationFixtures.createReservation(member, time2, theme);
        reservationDao.create(reservation1);
        reservationDao.create(reservation2);

        ReservationFilterRequest request = new ReservationFilterRequest(1L, 1L, "2024-04-04", "2024-04-03");

        //when //then
        assertThatThrownBy(() -> adminReservationService.findFiltered(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작 날짜는 종료 날짜보다 이후일 수 없습니다.");
    }
}