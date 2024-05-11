package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import roomescape.domain.Theme;

@JdbcTest
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeDaoTest {
    private final JdbcTemplate jdbcTemplate;
    private final ThemeDao themeDao;

    @Autowired
    ThemeDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.themeDao = new ThemeDao(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO member(name, email, password) VALUES ('켬미', 'aaa@naver.com', '1111')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "11:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "https://image.jpg");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마2", "설명2", "https://image.jpg");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마3", "설명3", "https://image.jpg");
        jdbcTemplate.update("INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES (?, ?, ?, ?)"
                , "2024-05-01", 1, 1, 2);
        jdbcTemplate.update("INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES (?, ?, ?, ?)"
                , "2024-04-30", 1, 1, 2);
        jdbcTemplate.update("INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES (?, ?, ?, ?)"
                , "2024-04-30", 1, 1, 1);
    }

    @DisplayName("DB에서 테마 목록을 읽을 수 있다.")
    @Test
    void readThemes() {
        List<Theme> actual = themeDao.readThemes();
        List<Theme> expected = List.of(
                new Theme(1L, "테마1", "설명1", "https://image.jpg"),
                new Theme(2L, "테마2", "설명2", "https://image.jpg"),
                new Theme(3L, "테마3", "설명3", "https://image.jpg")
        );
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("DB에서 특정 테마를 읽을 수 있다.")
    @Test
    void readThemeById() {
        Optional<Theme> actual = themeDao.readThemeById(1L);
        Theme expected = new Theme(1L, "테마1", "설명1", "https://image.jpg");
        assertThat(actual.get()).isEqualTo(expected);
    }

    @DisplayName("DB에서 없는 테마를 조회하려고하면 Optional 값이 넘어온다.")
    @Test
    void readThemeById_throwException() {
        Optional<Theme> actual = themeDao.readThemeById(4L);
        assertThat(actual.isEmpty()).isEqualTo(true);
    }

    @DisplayName("주어진 두 날짜 사이에 있는 예약을 기준으로 테마 랭킹을 읽을 수 있다.")
    @Test
    void readThemesRankingOfReservation() {
        List<Theme> actual = themeDao.readThemesRankingOfReservation("2024-04-29", "2024-05-02");
        List<Theme> expected = List.of(
                new Theme(2L, "테마2", "설명2", "https://image.jpg"),
                new Theme(1L, "테마1", "설명1", "https://image.jpg")
        );

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("테마 목록에 해당 테마 이름이 있는지 알 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"테마3, true", "테마4, false"})
    void existsThemeByName(String name, boolean expected) {
        boolean actual = themeDao.existsThemeByName(name);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("DB에 테마를 추가할 수 있다.")
    @Test
    void createTheme() {
        Theme theme = new Theme("테마4", "설명4", "https://image.jpg");

        themeDao.createTheme(theme);
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);

        assertThat(count).isEqualTo(4);
    }

    @DisplayName("DB에 테마를 삭제할 수 있다.")
    @Test
    void deleteTheme() {
        themeDao.deleteTheme(3L);
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from theme", Integer.class);

        assertThat(count).isEqualTo(2);
    }
}
