package roomescape.reservation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest(properties = "spring.sql.init.mode=never")
@Import(ReservationDao.class)
@Sql(scripts = "classpath:schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:reset-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationDaoTest {
    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 예약을_저장하고_ID로_조회할_수_있다() {
        Theme theme = createTheme("Theme A");
        ReservationTime reservationTime = createReservationTime(LocalTime.of(10, 0));

        Reservation saved = reservationDao.save("브라운", LocalDate.of(2026, 5, 1), reservationTime, theme);
        Reservation found = reservationDao.findById(saved.getId());

        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getName()).isEqualTo("브라운");
        assertThat(found.getDate()).isEqualTo(LocalDate.of(2026, 5, 1));
        assertThat(found.getTime().startAt()).isEqualTo(LocalTime.of(10, 0));
        assertThat(found.getTheme().name()).isEqualTo("Theme A");
    }

    @Test
    void 저장된_예약을_전체_조회할_수_있다() {
        Theme theme = createTheme("Theme A");
        ReservationTime firstTime = createReservationTime(LocalTime.of(10, 0));
        ReservationTime secondTime = createReservationTime(LocalTime.of(11, 0));

        reservationDao.save("브라운", LocalDate.of(2026, 5, 1), firstTime, theme);
        reservationDao.save("코니", LocalDate.of(2026, 5, 2), secondTime, theme);

        List<Reservation> reservations = reservationDao.findAll();

        assertThat(reservations).hasSize(2);
        assertThat(reservations)
                .extracting(Reservation::getName)
                .containsExactly("브라운", "코니");
    }

    @Test
    void ID로_예약을_삭제할_수_있다() {
        Theme theme = createTheme("Theme A");
        ReservationTime reservationTime = createReservationTime(LocalTime.of(10, 0));

        Reservation saved = reservationDao.save("브라운", LocalDate.of(2026, 5, 1), reservationTime, theme);

        reservationDao.delete(saved.getId());

        assertThat(reservationDao.findAll()).isEmpty();
    }

    @Test
    void 예약_시간_ID로_예약_수를_조회할_수_있다() {
        Theme theme = createTheme("Theme A");
        ReservationTime reservationTime = createReservationTime(LocalTime.of(10, 0));

        reservationDao.save("브라운", LocalDate.of(2026, 5, 1), reservationTime, theme);
        reservationDao.save("코니", LocalDate.of(2026, 5, 2), reservationTime, theme);

        int count = reservationDao.countByTimeId(reservationTime.id());

        assertThat(count).isEqualTo(2);
    }

    @Test
    void 날짜와_테마로_예약된_시간을_조회할_수_있다() {
        Theme theme = createTheme("Theme A");
        ReservationTime firstTime = createReservationTime(LocalTime.of(10, 0));
        ReservationTime secondTime = createReservationTime(LocalTime.of(11, 0));

        reservationDao.save("브라운", LocalDate.of(2026, 5, 1), firstTime, theme);
        reservationDao.save("코니", LocalDate.of(2026, 5, 1), secondTime, theme);

        List<Long> reservedTimes = reservationDao.findByDateAndTheme(LocalDate.of(2026, 5, 1), theme.id());

        assertThat(reservedTimes).containsExactly(firstTime.id(), secondTime.id());
    }

    private Theme createTheme(String name) {
        jdbcTemplate.update(
                "INSERT INTO themes (name, description, thumbnail) VALUES (?, ?, ?)",
                name,
                "desc",
                "https://example.com/a.png"
        );
        Long id = jdbcTemplate.queryForObject(
                "SELECT id FROM themes WHERE name = ?",
                Long.class,
                name
        );
        return new Theme(id, name, "desc", "https://example.com/a.png");
    }

    private ReservationTime createReservationTime(LocalTime startAt) {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", startAt);
        Long id = jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_at = ? ORDER BY id DESC LIMIT 1",
                Long.class,
                startAt
        );
        return new ReservationTime(id, startAt);
    }
}
