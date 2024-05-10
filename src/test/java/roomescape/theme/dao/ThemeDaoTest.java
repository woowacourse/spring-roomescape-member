package roomescape.theme.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
import roomescape.theme.domain.Theme;

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
    void findThemesTest() {
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨3 탈출", "레벨3 탈출하기", "https://img.jpg");
        List<Theme> expected = List.of(
                new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg"),
                new Theme(2L, "레벨3 탈출", "레벨3 탈출하기", "https://img.jpg"));

        List<Theme> actual = themeDao.findThemes();

        assertThat(actual).containsAll(expected);
    }

    @DisplayName("id를 통해 테마를 조회할 수 있다.")
    @Test
    void findThemeByIdTest() {
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        Optional<Theme> expected = Optional.of(
                new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg"));

        Optional<Theme> actual = themeDao.findThemeById(1L);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("해당 id의 테마가 없을 경우, 빈 값을 반환한다.")
    @Test
    void findThemeByIdTest_whenThemeNotExist() {
        Optional<Theme> actual = themeDao.findThemeById(1L);

        assertThat(actual).isEmpty();
    }

    @DisplayName("특정 기간 내 인기 테마를 예약 횟수 순으로 조회할 수 있다.")
    @Test
    void findThemesSortedByCountOfReservationTest() {
        jdbcTemplate.update("INSERT INTO member (name, email) VALUES (?, ?)", "브라운", "brown@abc.com");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨3 탈출", "레벨3 탈출하기", "https://img.jpg");
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00:00");
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) values (?, ?, ?, ?)",
                1, "2024-08-15", 1, 2);
        List<Theme> expected = List.of(new Theme(2L, "레벨3 탈출", "레벨3 탈출하기", "https://img.jpg"));

        LocalDate start = LocalDate.of(2024, 8, 14);
        LocalDate end = LocalDate.of(2024, 8, 15);
        int count = 1;
        List<Theme> actual = themeDao.findThemesSortedByCountOfReservation(start, end, count);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("테마를 생성할 수 있다.")
    @Test
    void createThemeTest() {
        Theme theme = new Theme("레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        Theme expected = new Theme(1L, "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");

        Theme actual = themeDao.createTheme(theme);

        assertAll(
                () -> assertThat(actual).isEqualTo(expected),
                () -> assertThat(countSavedTheme()).isEqualTo(1)
        );
    }

    @DisplayName("이미 해당 테마 이름이 존재한다면, 테마를 생성할 수 없다.")
    @Test
    void createThemeTest_whenNameIsAlreadyExist() {
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        Theme theme = new Theme("레벨2 탈출", "레벨2 탈출하기 대작전", "https://img.jpg");

        assertThatThrownBy(() -> themeDao.createTheme(theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테마 이름은 이미 존재합니다.");
    }

    @DisplayName("테마를 삭제할 수 있다.")
    @Test
    void deleteThemeTest() {
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");

        themeDao.deleteTheme(1L);

        assertThat(countSavedTheme()).isZero();
    }

    @DisplayName("해당 테마에 예약이 있다면, 예약 시간을 삭제할 수 없다.")
    @Test
    void deleteThemeTest_whenReservationUsingThemeExist() {
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)",
                "레벨2 탈출", "레벨2 탈출하기", "https://img.jpg");
        jdbcTemplate.update("INSERT INTO member (name, email) VALUES (?, ?)", "브라운", "brown@abc.com");
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "19:00:00");
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) values (?, ?, ?, ?)",
                1, "2024-08-15", 1, 1);

        assertThatThrownBy(() -> themeDao.deleteTheme(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테마는 이미 예약되어 있어 삭제할 수 없습니다.");
    }

    private int countSavedTheme() {
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM theme", Integer.class);
    }
}
