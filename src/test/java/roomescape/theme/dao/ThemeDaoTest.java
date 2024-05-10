package roomescape.theme.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import roomescape.member.domain.Member;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.theme.domain.Theme;
import roomescape.time.dao.ReservationTimeDao;
import roomescape.time.domain.ReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/schema.sql"})
class ThemeDaoTest {

    @Autowired
    private ReservationTimeDao reservationTimeDao;
    @Autowired
    private ThemeDao themeDao;
    @Autowired
    private ReservationDao reservationDao;

    private final Member memberOne = new Member(1L, "hotea", "hotea@hotea.com");
    private final Member memberTwo = new Member(2L, "zeus", "zues@zeus.com");
    private final Member memberThree = new Member(3L, "zh", "zh@zh.com");
    private final ReservationTime reservationTime = new ReservationTime(1L, "10:00");
    private final Theme theme = new Theme(1L, "정글 모험", "열대 정글의 심연을 탐험하세요.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    private final Reservation reservation = new Reservation(memberOne, new ReservationDate("2024-04-27"), reservationTime, theme);


    @DisplayName("인기 테마를 조회할 수 있다.")
    @Test
    void findPopularThemes() {
        Theme secondTheme = new Theme(2L, "정글 모험2", "열대 정글의 심연을 탐험하세요.", "2");
        Theme thirdTheme = new Theme(3L, "정글 모험3", "열대 정글의 심연을 탐험하세요.", "3");
        Reservation secondReservation = new Reservation(memberOne, new ReservationDate("2024-04-26"), reservationTime, theme);
        Reservation thirdReservation = new Reservation(memberTwo, new ReservationDate("2024-04-29"), reservationTime, secondTheme);
        Reservation fourthReservation = new Reservation(memberThree, new ReservationDate("2024-04-30"), reservationTime, secondTheme);

        themeDao.save(theme);
        themeDao.save(secondTheme);
        themeDao.save(thirdTheme);
        reservationTimeDao.save(reservationTime);
        reservationDao.save(reservation);
        reservationDao.save(secondReservation);
        reservationDao.save(thirdReservation);
        reservationDao.save(fourthReservation);

        List<Theme> themeList = themeDao.findPopularThemes(LocalDate.parse("2024-04-24"), LocalDate.parse("2024-04-30"));
        List<Long> actual = themeList.stream().map(Theme::getId).toList();
        List<Long> expected = List.of(1L, 2L);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("특정 테마가 이미 존재하는지 알 수 있다.")
    @Test
    void checkExistThemes() {
        themeDao.save(theme);

        assertThat(themeDao.checkExistThemes(theme)).isTrue();
    }
}
