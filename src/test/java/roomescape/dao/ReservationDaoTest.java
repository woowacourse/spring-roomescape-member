package roomescape.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.support.DatabaseCleanUp;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ReservationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private ReservationDao reservationDao;

    private ReservationTime reservationTime;

    private Theme theme;

    @BeforeEach
    void beforeEach() {
        String insertReservationTimeSql = "INSERT INTO `reservation_time` (`start_at`) VALUES (?)";
        jdbcTemplate.update(insertReservationTimeSql, "10:00");
        jdbcTemplate.update(insertReservationTimeSql, "11:00");

        String insertThemeSql = "INSERT INTO `theme` (`name`, `description`, `thumbnail_url`) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertThemeSql, "방탈출1", "방탈출1 설명", "url.jpg");

        reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        theme = new Theme(1L, "방탈출1", "방탈출1 설명", "url.jpg");
    }

    @Test
    void createTest() {
        Reservation reservationWithoutId = new Reservation("fizz", LocalDate.of(2026, 5, 2), reservationTime, theme);

        Reservation reservation = reservationDao.create(reservationWithoutId);

        assertThat(reservation.getId()).isEqualTo(1L);
    }

    @Test
    void readAllTest() {
        String sql = "INSERT INTO `reservation` (`name`, `date`, `time_id`, `theme_id`) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, "fizz", "2026-05-02", 1L, 1L);
        jdbcTemplate.update(sql, "fizz", "2026-05-02", 2L, 1L);

        List<Reservation> reservations = reservationDao.readAll();

        assertThat(reservations.size()).isEqualTo(2);
    }

    @Test
    void deleteTest() {
        String sql = "INSERT INTO `reservation` (`name`, `date`, `time_id`, `theme_id`) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, "fizz", "2026-05-02", 1L, 1L);

        reservationDao.delete(1L);

        String readReservationCountSql = "SELECT COUNT(*) FROM `reservation`";
        int count = jdbcTemplate.queryForObject(readReservationCountSql, Integer.class);

        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    void existByDateAndTimeIdAndThemeIdTest() {
        boolean exist = reservationDao.existByDateAndTimeIdAndThemeId(LocalDate.of(2026, 5, 2), 1L, 1L);
        assertThat(exist).isFalse();

        String sql = "INSERT INTO `reservation` (`name`, `date`, `time_id`, `theme_id`) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, "fizz", "2026-05-02", 1L, 1L);

        exist = reservationDao.existByDateAndTimeIdAndThemeId(LocalDate.of(2026, 5, 2), 1L, 1L);
        assertThat(exist).isTrue();
    }

    @Test
    void existByTimeIdTest() {
        boolean exist = reservationDao.existByTimeId(1L);
        assertThat(exist).isFalse();

        String sql = "INSERT INTO `reservation` (`name`, `date`, `time_id`, `theme_id`) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, "fizz", "2026-05-02", 1L, 1L);

        exist = reservationDao.existByTimeId(1L);
        assertThat(exist).isTrue();
    }

    @Test
    void existByThemeIdTest() {
        boolean exist = reservationDao.existByThemeId(1L);
        assertThat(exist).isFalse();

        String sql = "INSERT INTO `reservation` (`name`, `date`, `time_id`, `theme_id`) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, "fizz", "2026-05-02", 1L, 1L);

        exist = reservationDao.existByThemeId(1L);
        assertThat(exist).isTrue();
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.execute();
    }
}
