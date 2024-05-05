package roomescape.dao;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationDaoTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    void existByDateTime() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니", "2024-05-01", 1, 1);
        boolean existByDateTimeTheme = reservationDao.existByDateTimeTheme(LocalDate.of(2024, 5, 1), LocalTime.of(10, 0), 1L);
        assertThat(existByDateTimeTheme).isTrue();
    }

    @Test
    void findThemeIdsOrderByReservationThemeCountDesc() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate sevenDaysBefore = LocalDate.now().minusDays(7);
        for (int i = 2; i <= 12; i++) {
            jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름" + i, "설명", "썸네일");
            jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니", yesterday, 1, i);
            jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니", sevenDaysBefore, 1, i);
        }
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름13", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니", yesterday, 1, 13);

        List<Long> themeIds = reservationDao.find10ThemeIdsOrderByReservationThemeCountDescBetween(sevenDaysBefore, yesterday);
        assertThat(themeIds).doesNotContain(13L);
    }
}
