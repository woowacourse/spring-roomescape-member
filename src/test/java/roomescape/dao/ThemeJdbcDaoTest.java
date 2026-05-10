package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.row.AvailableTimeRow;
import roomescape.dao.row.ReservationRow;
import roomescape.dao.row.ThemeRow;
import roomescape.dao.row.TimeRow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({
        ThemeJdbcDao.class,
        ReservationJdbcDao.class,
        TimeJdbcDao.class
})
@ActiveProfiles("test")
class ThemeJdbcDaoTest {
    private static final int DELETED = 1;

    @Autowired
    private ThemeDao themeDao;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private TimeDao timeDao;

    private ThemeRow theme1;
    private ThemeRow theme2;

    private TimeRow time1;
    private TimeRow time2;

    @BeforeEach
    void setUp() {
        time1 = timeDao.create(new TimeRow(LocalTime.parse("10:00")));
        time2 = timeDao.create(new TimeRow(LocalTime.parse("12:00")));

        theme1 = new ThemeRow("방 이름", "url", "설명");
        theme2 = new ThemeRow("두번째 방이름", "url2", "설명2");
    }

    @Test
    void findAll() {
        List<ThemeRow> saved = insertThemesHandler(theme1, theme2);
        List<ThemeRow> find = themeDao.findAll();

        assertThat(find).hasSize(saved.size())
                .containsAll(saved);
    }

    @Test
    void findById() {
        ThemeRow saved = createThemeHandler(theme1);

        assertThat(themeDao.findById(saved.id()))
                .isPresent()
                .get().isEqualTo(saved);
    }

    @Test
    void create() {
        ThemeRow saved = createThemeHandler(theme1);
        assertThat(saved).isNotNull();
    }

    @Test
    void delete() {
        ThemeRow saved = createThemeHandler(theme1);
        assertThat(themeDao.delete(saved.id())).isEqualTo(DELETED);
    }

    @Test
    void existsByName() {
        ThemeRow saved = createThemeHandler(theme1);
        ThemeRow notExists = theme2;

        assertThat(themeDao.existsByName(saved.name())).isTrue();
        assertThat(themeDao.existsByName(notExists.name())).isFalse();
    }

    @Test
    void findAvailableTimesById() {
        ThemeRow saved = createThemeHandler(theme1);
        ReservationRow reservaton = new ReservationRow("이름1", LocalDate.parse("2026-05-05"), time1, saved);

        reservationDao.create(reservaton);

        Long themeId = saved.id();
        LocalDate localDate = LocalDate.of(2026, 5, 5);

        List<AvailableTimeRow> expected = List.of(
                new AvailableTimeRow(time2.id(), time2.startAt(), false),
                new AvailableTimeRow(time1.id(), time1.startAt(), true));

        List<AvailableTimeRow> availableTimesById = themeDao.findAvailableTimesById(themeId, localDate);

        assertThat(availableTimesById).containsAll(expected);
    }

    @Test
    void findPopulars() {
        int limit = 1;
        int days = 7;
        LocalDate now = LocalDate.now();

        ThemeRow popular = createThemeHandler(theme1);
        ThemeRow nonPopular = createThemeHandler(theme2);

        reservationDao.create(new ReservationRow("이름1", LocalDate.parse("2026-05-05"), time1, popular));
        List<ThemeRow> populars = themeDao.findPopulars(limit, days, now);

        assertThat(populars)
                .hasSize(limit)
                .contains(popular);
    }

    @Test
    void existsById() {
        ThemeRow saved = createThemeHandler(theme1);
        Long notExists = 2L;

        assertThat(themeDao.existsById(saved.id())).isTrue();
        assertThat(themeDao.existsById(notExists)).isFalse();
    }

    private List<ThemeRow> insertThemesHandler(ThemeRow... themes) {
        return Arrays.stream(themes)
                .map(this::createThemeHandler)
                .toList();
    }

    private ThemeRow createThemeHandler(ThemeRow theme) {
        return themeDao.create(theme);
    }

}
