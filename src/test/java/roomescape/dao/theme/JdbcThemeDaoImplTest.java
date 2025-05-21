package roomescape.dao.theme;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.domain.theme.Theme;

class JdbcThemeDaoImplTest {

    private DataSource datasource;
    private JdbcTemplate jdbcTemplate;
    private JdbcThemeDaoImpl themeDao;

    @BeforeEach
    void init() {
        datasource = new EmbeddedDatabaseBuilder()
                .setName("testdb-" + UUID.randomUUID())
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(datasource);
        themeDao = new JdbcThemeDaoImpl(jdbcTemplate);
    }

    @DisplayName("테마를 저장할 수 있다.")
    @Test
    void save() {
        //given
        Theme theme = new Theme("name", "description", "thumbnail");

        //when
        themeDao.saveTheme(theme);

        //then
        List<Theme> actual = themeDao.findAllTheme();
        assertThat(actual).hasSize(1);
        assertThat(theme.getId()).isEqualTo(1L);
    }

    @DisplayName("테마 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        //given
        themeDao.saveTheme(new Theme("name", "description", "thumbnail"));
        themeDao.saveTheme(new Theme("name1", "description1", "thumbnail1"));

        //when
        List<Theme> actual = themeDao.findAllTheme();

        //then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("테마를 삭제할 수 있다.")
    @Test
    void delete() {
        //given
        Theme theme = new Theme("name", "description", "thumbnail");
        themeDao.saveTheme(theme);

        //when
        themeDao.deleteTheme(theme.getId());

        //then
        List<Theme> actual = themeDao.findAllTheme();
        assertThat(actual).isEmpty();
    }

    @DisplayName("특정 테마를 조회할 수 있다.")
    @Test
    void findById() {
        //given
        Theme theme = new Theme("name", "description", "thumbnail");
        themeDao.saveTheme(theme);

        //when
        Optional<Theme> actual = themeDao.findById(theme.getId());

        //then
        assertThat(actual).isEqualTo(Optional.of(theme));
    }

    @DisplayName("예약이 많은 테마 10개를 조회할 수 있다.")
    @Test
    void findAllThemeOfRankBy() {
        //given
        createTheme();
        createReservationTime();
        createMember();
        createReservation();

        LocalDate currentDate = LocalDate.of(2025, 4, 8);
        LocalDate startDate = LocalDate.of(2025, 4, 1);
        int limitCount = 10;

        //when
        List<Theme> actual = themeDao.findAllThemeOfRankBy(startDate, currentDate, limitCount);

        //then
        assertThat(actual).hasSize(10);
    }

    private void createTheme() {
        for (int i = 1; i <= 13; i++) {
            themeDao.saveTheme(new Theme("a" + i, "a", "b"));
        }
    }

    private void createReservationTime() {
        for (int i = 1; i <= 8; i++) {
            jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", i, "11:59");
        }
    }

    private void createMember() {
        for (int i = 1; i < 21; i++) {
            jdbcTemplate.update("INSERT INTO member (id, name, email, password, role) VALUES (?, ?, ?, ?, ?)",
                    i,
                    "도기",
                    "testEmail" + i,
                    "1234",
                    "basic"
            );
        }
    }

    private void createReservation() {
        Object[][] reservations = {
                {1, 1, "2025-04-01", 1},
                {2, 2, "2025-04-02", 1},
                {3, 3, "2025-04-03", 1},
                {4, 4, "2025-04-04", 1},
                {5, 5, "2025-04-05", 2},
                {6, 6, "2025-04-06", 2},
                {7, 7, "2025-04-07", 3},
                {8, 8, "2025-04-01", 3},
                {9, 9, "2025-04-02", 3},
                {10, 10, "2025-04-03", 4},
                {11, 11, "2025-04-04", 5},
                {12, 12, "2025-04-05", 6},
                {13, 13, "2025-04-06", 7},
                {14, 14, "2025-04-07", 8},
                {15, 15, "2025-04-01", 9},
                {16, 16, "2025-04-02", 9},
                {17, 17, "2025-04-03", 11},
                {18, 18, "2025-04-04", 12},
                {19, 19, "2025-04-05", 2},
                {20, 20, "2025-04-06", 3}
        };

        for (Object[] reservation : reservations) {
            jdbcTemplate.update(
                    "INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?, ?)",
                    reservation[0], reservation[1], reservation[2], 1, reservation[3]
            );
        }
    }
}
