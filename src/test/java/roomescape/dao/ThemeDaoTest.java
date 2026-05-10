package roomescape.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeDaoTest {

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ReservationDao reservationDao;

    @Test
    void 테마를_저장하고_id로_조회한다() {
        Theme savedTheme = themeDao.save(new Theme("공포", "무서운 테마", "thumbnail"));

        Theme foundTheme = themeDao.findById(savedTheme.getId());

        assertThat(foundTheme.getId()).isEqualTo(savedTheme.getId());
        assertThat(foundTheme.getName()).isEqualTo("공포");
        assertThat(foundTheme.getDescription()).isEqualTo("무서운 테마");
        assertThat(foundTheme.getThumbnail()).isEqualTo("thumbnail");
    }


    @Test
    void 이름으로_테마_존재_여부를_확인한다() {
        themeDao.save(new Theme("공포", "무서운 테마", "thumbnail"));

        assertThat(themeDao.existsByName("공포")).isTrue();
        assertThat(themeDao.existsByName("판타지")).isFalse();
    }

    @Test
    void 기간_내_예약수를_기준으로_인기_테마를_조회한다() {
        ReservationTime time = reservationTimeDao.save(new ReservationTime(LocalTime.of(10, 0)));

        Theme theme1 = themeDao.save(new Theme("공포", "무서운 테마", "thumbnail1"));
        Theme theme2 = themeDao.save(new Theme("판타지", "신비한 테마", "thumbnail2"));
        Theme theme3 = themeDao.save(new Theme("추리", "추리 테마", "thumbnail3"));

        LocalDate startDate = LocalDate.of(2030, 5, 1);
        LocalDate endDate = LocalDate.of(2030, 5, 7);

        reservationDao.save(new Reservation("브라운", LocalDate.of(2030, 5, 1), time, theme1));
        reservationDao.save(new Reservation("코니", LocalDate.of(2030, 5, 2), time, theme1));
        reservationDao.save(new Reservation("포비", LocalDate.of(2030, 5, 3), time, theme2));

        reservationDao.save(new Reservation("루피", LocalDate.of(2030, 4, 30), time, theme3));
        reservationDao.save(new Reservation("초코", LocalDate.of(2030, 5, 8), time, theme3));

        List<PopularThemeResult> popularThemes = themeDao.findPopularThemes(startDate, endDate, 10);

        assertThat(popularThemes)
                .extracting(result -> result.theme().getName())
                .containsExactly("공포", "판타지");
    }

    @Test
    void 인기_테마는_전달한_limit_개수만큼_조회한다() {
        ReservationTime time1 = reservationTimeDao.save(new ReservationTime(LocalTime.of(10, 0)));
        ReservationTime time2 = reservationTimeDao.save(new ReservationTime(LocalTime.of(11, 0)));
        ReservationTime time3 = reservationTimeDao.save(new ReservationTime(LocalTime.of(12, 0)));

        Theme theme1 = themeDao.save(new Theme("공포", "무서운 테마", "thumbnail1"));
        Theme theme2 = themeDao.save(new Theme("판타지", "신비한 테마", "thumbnail2"));
        Theme theme3 = themeDao.save(new Theme("추리", "추리 테마", "thumbnail3"));

        LocalDate startDate = LocalDate.of(2030, 5, 1);
        LocalDate endDate = LocalDate.of(2030, 5, 7);

        reservationDao.save(new Reservation("브라운", LocalDate.of(2030, 5, 1), time1, theme1));
        reservationDao.save(new Reservation("코니", LocalDate.of(2030, 5, 2), time2, theme2));
        reservationDao.save(new Reservation("포비", LocalDate.of(2030, 5, 3), time3, theme3));

        List<PopularThemeResult> popularThemes = themeDao.findPopularThemes(startDate, endDate, 2);

        assertThat(popularThemes).hasSize(2);
    }

}
