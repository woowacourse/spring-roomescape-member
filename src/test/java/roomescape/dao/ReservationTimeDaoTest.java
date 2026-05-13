package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;
import roomescape.support.DatabaseCleanUp;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ReservationTimeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Test
    void createTest() {
        ReservationTime reservationTimeWithoutId = new ReservationTime(LocalTime.of(10, 0));
        ReservationTime reservationTime = reservationTimeDao.create(reservationTimeWithoutId);

        assertThat(reservationTime.getId()).isEqualTo(1L);
    }

    @Test
    void readTest() {
        String sql = "INSERT INTO `reservation_time` (`start_at`) VALUES (?)";
        jdbcTemplate.update(sql, "10:00");

        Optional<ReservationTime> reservationTime = reservationTimeDao.read(1L);

        assertThat(reservationTime.orElseThrow().getId()).isEqualTo(1L);
    }

    @Test
    void readAllTest() {
        String sql = "INSERT INTO `reservation_time` (`start_at`) VALUES (?)";
        jdbcTemplate.update(sql, "10:00");
        jdbcTemplate.update(sql, "11:00");

        List<ReservationTime> reservationTimes = reservationTimeDao.readAll();
        assertThat(reservationTimes.size()).isEqualTo(2);
    }

    @Test
    void reservedTimeIdByDateAndThemeTest() {
        String insertReservationTimeSql = "INSERT INTO `reservation_time` (`start_at`) VALUES (?)";
        jdbcTemplate.update(insertReservationTimeSql, "10:00");
        jdbcTemplate.update(insertReservationTimeSql, "11:00");
        jdbcTemplate.update(insertReservationTimeSql, "12:00");

        String insertThemeSql = "INSERT INTO `theme` (`name`, `description`, `thumbnail_url`) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertThemeSql, "방탈출1", "방탈출1 설명", "url.jpg");

        String insertReservationSql = "INSERT INTO `reservation` (`name`, `date`, `time_id`, `theme_id`) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertReservationSql, "fizz", "2026-05-02", 1L, 1L);
        jdbcTemplate.update(insertReservationSql, "fizz", "2026-05-02", 2L, 1L);

        List<Long> reservedTimeIds = reservationTimeDao.reservedTimeIdByDateAndTheme(LocalDate.of(2026, 5, 2), 1L);

        assertThat(reservedTimeIds.get(0)).isEqualTo(1L);
        assertThat(reservedTimeIds.get(1)).isEqualTo(2L);
    }

    @Test
    void deleteTest() {
        String insertReservationTimeSql = "INSERT INTO `reservation_time` (`start_at`) VALUES (?)";
        jdbcTemplate.update(insertReservationTimeSql, "10:00");

        reservationTimeDao.delete(1L);

        String readAllReservationTimeCountSql = "SELECT COUNT(*) FROM `reservation_time`";
        int count = jdbcTemplate.queryForObject(readAllReservationTimeCountSql, Integer.class);

        assertThat(count).isEqualTo(0);
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.execute();
    }
}
