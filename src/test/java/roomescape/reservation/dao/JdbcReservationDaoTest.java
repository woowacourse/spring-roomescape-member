package roomescape.reservation.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.Theme;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.fixture.MemberFixture.memberFixture;
import static roomescape.fixture.ReservationTimeFixture.reservationTimeFixture;
import static roomescape.fixture.ThemeFixture.themeFixture;

@JdbcTest
class JdbcReservationDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcReservationDao jdbcReservationDao;
    private final LocalDateTime createdAt = LocalDateTime.of(2024, 5, 8, 12, 30);

    @Autowired
    private JdbcReservationDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcReservationDao = new JdbcReservationDao(jdbcTemplate);
    }

    @BeforeEach
    void insert() {
        insertReservationTime();
        insertTheme();
    }

    private void insertTheme() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", themeFixture.getName(), themeFixture.getDescription(), themeFixture.getThumbnail());
    }

    private void insertReservationTime() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", reservationTimeFixture.getStartAt());
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
        Reservation reservation = new Reservation(null, memberFixture, LocalDate.of(2999, 8, 5), reservationTimeFixture, themeFixture, createdAt);
        jdbcReservationDao.save(reservation);
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("DB 모든 예약 조회 테스트")
    @Test
    void findAllReservations() {
        String sql = """
                INSERT INTO reservation (member_id, date, time_id, theme_id, created_at)\s
                VALUES (?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql, 1, "2999-12-12", 1, 1, "2024-05-08 12:30");
        List<Reservation> reservations = jdbcReservationDao.findAllReservations();
        assertThat(reservations).hasSize(1);
    }

    @DisplayName("DB 예약 삭제 테스트")
    @Test
    void delete() {
        String sql = """
                INSERT INTO reservation (member_id, date, time_id, theme_id, created_at)\s
                VALUES (?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql, 1, "2999-12-12", 1, 1, "2024-05-08 12:30");
        jdbcReservationDao.delete(1L);
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);
        assertThat(count).isEqualTo(0);
    }
}
