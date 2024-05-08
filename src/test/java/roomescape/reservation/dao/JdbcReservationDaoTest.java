package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

@JdbcTest
class JdbcReservationDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcReservationDao jdbcReservationDao;

    private Reservation savedReservation;

    @Autowired
    private JdbcReservationDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcReservationDao = new JdbcReservationDao(jdbcTemplate);
    }

    @BeforeEach
    void saveReservation() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", LocalTime.of(10, 0));
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES(?, ?, ?)", "happy", "hi", "abcd.html");
        Theme theme = new Theme(1L, "happy", "hi", "abcd.html");
        LocalDateTime createdAt = LocalDateTime.of(2024, 5, 8, 12, 30);
        Reservation reservation = new Reservation(null, "parang", LocalDate.of(2999, 3, 28), reservationTime, theme, createdAt);
        savedReservation = jdbcReservationDao.save(reservation);
    }

    @AfterEach
    void setUp() {
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN `id` RESTART");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN `id` RESTART");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN `id` RESTART");
    }

    @DisplayName("DB 예약 추가 테스트")
    @Test
    void save() {
        Assertions.assertThat(savedReservation.getName()).isEqualTo("parang");
    }

    @DisplayName("DB 모든 예약 조회 테스트")
    @Test
    void findAllReservations() {
        List<Reservation> reservations = jdbcReservationDao.findAllReservations();
        assertThat(reservations).hasSize(1);
    }

    @DisplayName("DB 예약 삭제 테스트")
    @Test
    void delete() {
        jdbcReservationDao.delete(savedReservation.getId());
        List<Reservation> reservations = jdbcReservationDao.findAllReservations();
        assertThat(reservations).isEmpty();
    }
}
