package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.PopularThemeResult;

@JdbcTest
@Import({ThemeDao.class, ReservationDao.class, ReservationTimeDao.class})
class ThemeDaoTest {

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Test
    void 저장_후_ID_생성_확인() {
        //given
        Theme theme = new Theme("공포", "무서움", "https://roomescape.com");
        Theme savedTheme = themeDao.save(theme);

        //when
        Theme loadTheme = themeDao.findById(savedTheme.getId());

        //then
        assertThat(loadTheme.getId()).isEqualTo(savedTheme.getId());
        assertThat(loadTheme.getName()).isEqualTo(savedTheme.getName());
    }

    @Test
    void 같은이름_존재여부_확인() {
        //given
        Theme theme = new Theme("공포", "무서움", "https://roomescape.com");
        themeDao.save(theme);
        Theme newTheme = new Theme("공포", "덜무서움", "https://roomescape.com");

        //when
        boolean existName = themeDao.existsByName(newTheme.getName());

        //then
        assertThat(existName).isTrue();
    }

    @Test
    void 일주일간_인기테마_확인() {
        //given
        LocalDate startDate = LocalDate.parse("2026-05-11");
        LocalDate endDate = LocalDate.parse("2026-05-17");
        Theme horrorTheme = themeDao.save(new Theme("공포", "무서움", "https://roomescape.com"));
        Theme hospitalTheme = themeDao.save(new Theme("병원", "병원을 탈출하라", "https://roomescape.com"));
        Theme zombieTheme = themeDao.save(new Theme("좀비", "좀비에게서 생존하라", "https://roomescape.com"));

        ReservationTime time = reservationTimeDao.save(new ReservationTime(LocalTime.parse("10:00")));

        reservationDao.save(new Reservation("피노", LocalDate.parse("2026-05-12"), time, horrorTheme));
        reservationDao.save(new Reservation("네오", LocalDate.parse("2026-05-13"), time, horrorTheme));
        reservationDao.save(new Reservation("포비", LocalDate.parse("2026-05-14"), time, horrorTheme));
        reservationDao.save(new Reservation("브라운", LocalDate.parse("2026-05-15"), time, hospitalTheme));

        //when
        List<PopularThemeResult> result = themeDao.findPopularThemes(startDate, endDate);

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).theme().getName()).isEqualTo("공포");
        assertThat(result.get(1).theme().getName()).isEqualTo("병원");
    }
}
