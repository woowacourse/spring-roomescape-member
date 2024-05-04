package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;

@JdbcTest
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ThemeDao themeDao;

    @BeforeEach
    void setUp() {
        themeDao = new ThemeDao(jdbcTemplate);
    }

    @DisplayName("모든 테마를 조회할 수 있다.")
    @Test
    void readThemesTest() {
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨3 탈출", "레벨3 탈출하기", "https://img.jpg");
        List<Theme> expected = List.of(
                new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg"),
                new Theme(2L, "레벨3 탈출", "레벨3 탈출하기", "https://img.jpg"));

        List<Theme> actual = themeDao.readThemes();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("id를 통해 테마를 조회할 수 있다.")
    @Test
    void readThemeById() {
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        Optional<Theme> expected = Optional.of(
                new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg"));

        Optional<Theme> actual = themeDao.readThemeById(1L);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("해당 id의 테마가 없을 경우, 빈 값을 반환한다.")
    @Test
    void readThemeById_whenThemeNotExist() {
        Optional<Theme> actual = themeDao.readThemeById(1L);

        assertThat(actual).isEmpty();
    }

    @DisplayName("특정 기간 내 인기 테마를 예약 횟수 순으로 조회할 수 있다.")
    @Test
    void readThemesSortedByCountOfReservation() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨3 탈출", "레벨3 탈출하기", "https://img.jpg");
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)",
                "브라운", "2024-08-15", 1, 2);
        List<Theme> expected = List.of(new Theme(2L, "레벨3 탈출", "레벨3 탈출하기", "https://img.jpg"));

        LocalDate start = LocalDate.of(2024, 8, 14);
        LocalDate end = LocalDate.of(2024, 8, 15);
        int count = 1;
        List<Theme> actual = themeDao.readThemesSortedByCountOfReservation(start, end, count);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void isExistThemeByName() {
    }

    @Test
    void createTheme() {
    }

    @Test
    void deleteTheme() {
    }
}
