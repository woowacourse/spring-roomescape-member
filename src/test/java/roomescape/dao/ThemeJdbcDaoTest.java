package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.jdbc.ReservationJdbcDao;
import roomescape.dao.jdbc.ThemeJdbcDao;
import roomescape.dao.jdbc.TimeJdbcDao;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.vo.Name;
import roomescape.dto.request.PopularThemeRequestDto;
import roomescape.dto.response.AvailableTimeResponseDto;

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

    private Theme theme1;
    private Theme theme2;

    private Time time1;
    private Time time2;

    @BeforeEach
    void setUp() {
        time1 = timeDao.insert(new Time(LocalTime.parse("10:00")));
        time2 = timeDao.insert(new Time(LocalTime.parse("12:00")));

        theme1 = new Theme(new Name("방 이름"), "url", "설명");
        theme2 = new Theme(new Name("두번째 방이름"), "url2", "설명2");
    }

    @Test
    void findAll() {
        List<Theme> saved = insertThemesHandler(theme1, theme2);
        List<Theme> find = themeDao.findAll();

        assertThat(find).hasSize(saved.size())
                .containsAll(saved);
    }

    @Test
    void findById() {
        Theme saved = insertThemeHandler(theme1);

        assertThat(themeDao.findById(saved.getId()))
                .isPresent()
                .get().isEqualTo(saved);
    }

    @Test
    void insert() {
        Theme saved = insertThemeHandler(theme1);
        assertThat(saved).isNotNull();
    }

    @Test
    void delete() {
        Theme saved = insertThemeHandler(theme1);
        assertThat(themeDao.delete(saved.getId())).isEqualTo(DELETED);
    }


    private List<Theme> insertThemesHandler(Theme... themes) {
        return Arrays.stream(themes)
                .map(this::insertThemeHandler)
                .toList();
    }

    private Theme insertThemeHandler(Theme theme) {
        return themeDao.insert(theme);
    }

    @Test
    void existsByName() {
        Theme saved = insertThemeHandler(theme1);
        Theme notExists = theme2;
        assertThat(themeDao.existsByName(saved.getName())).isTrue();
        assertThat(themeDao.existsByName(notExists.getName())).isFalse();
    }

    @Test
    void findAvailableTimesById() {
        Theme saved = insertThemeHandler(theme1);
        Reservation reservaton = new Reservation("이름1", LocalDate.parse("2026-05-05"), time1, saved);

        reservationDao.insert(reservaton);

        Long themeId = saved.getId();
        LocalDate localDate = LocalDate.of(2026, 5, 5);

        List<AvailableTimeResponseDto> expected = List.of(
                new AvailableTimeResponseDto(time2.getId(), time2.getStartAt(), false),
                new AvailableTimeResponseDto(time1.getId(), time1.getStartAt(), true));

        List<AvailableTimeResponseDto> availableTimesById = themeDao.findAvailableTimesById(themeId, localDate);

        assertThat(availableTimesById).containsAll(expected);
    }

    @Test
    void findPopulars() {
        int limit = 1;

        PopularThemeRequestDto popularThemeRequestDto = new PopularThemeRequestDto(limit, 7);
        Theme popular = insertThemeHandler(theme1);
        Theme nonPopular = insertThemeHandler(theme2);

        reservationDao.insert(new Reservation("이름1", LocalDate.parse("2026-05-05"), time1, popular));
        List<Theme> populars = themeDao.findPopulars(
                LocalDate.parse("2026-05-05"),
                LocalDate.parse("2026-05-05"),
                limit);

        assertThat(populars)
                .hasSize(limit)
                .contains(popular);
    }
}
