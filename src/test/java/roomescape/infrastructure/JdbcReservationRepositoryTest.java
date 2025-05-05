package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import(JdbcReservationRepository.class)
@ActiveProfiles("test")
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute((Connection connection) -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute("SET REFERENTIAL_INTEGRITY FALSE");
                statement.execute("TRUNCATE TABLE reservation");
                statement.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
                statement.execute("TRUNCATE TABLE reservation_time");
                statement.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
                statement.execute("TRUNCATE TABLE theme");
                statement.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
                statement.execute("SET REFERENTIAL_INTEGRITY TRUE");
            }
            return null;
        });
    }

    @DisplayName("save 후 생성된 id를 반환한다.")
    @Test
    void saveTest() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (1, '10:00')");
        jdbcTemplate.update(
                "INSERT INTO theme (id, name, description, thumbnail) VALUES (1, '테마1', '테마 1입니다.', '썸네일입니다.')");

        Reservation reservation = Reservation.withoutId(
                "테스트행님",
                Theme.of(1L, "테마1", "테마 1입니다.", "썸네일"),
                LocalDate.of(2025, 4, 30),
                ReservationTime.of(1L, LocalTime.of(10, 0))
        );

        // when
        Long savedId = reservationRepository.save(reservation);

        // then
        String sql = "SELECT * FROM reservation WHERE id = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, savedId);

        assertAll(
                () -> assertThat(savedId).isNotNull(),

                () -> assertThat(result).containsEntry("name", "테스트행님"),
                () -> assertThat(result).containsEntry("date", Date.valueOf(LocalDate.of(2025, 4, 30))),
                () -> assertThat(result).containsEntry("time_id", 1L)
        );
    }

    @DisplayName("모든 예약을 조회할 수 있다.")
    @Test
    void findAllTest() {
        // given
        jdbcTemplate.update(
                "INSERT INTO theme (id, name, description, thumbnail) VALUES (1, '테마1', '테마 1입니다.', '썸네일입니다.')");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (1, '10:00:00')");

        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('브라운', '2025-01-01', 1, 1)");
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('솔라', '2025-01-01', 1, 1)");
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('브리', '2025-01-01', 1, 1)");

        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations).hasSize(3);
        assertThat(reservations)
                .extracting(Reservation::getName)
                .containsExactly("브라운", "솔라", "브리");
    }

    @DisplayName("id로 예약을 삭제할 수 있다.")
    @Test
    void deleteByIdTest() {
        // given
        assertThat(reservationRepository.findAll()).isEmpty();
        jdbcTemplate.update(
                "INSERT INTO theme (id, name, description, thumbnail) VALUES (1, '테마1', '테마 1입니다.', '썸네일입니다.')");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (1, '10:00:00')");
        jdbcTemplate.update(
                "INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (1, '브라운', '2025-01-01', 1, 1)");
        assertThat(reservationRepository.findAll()).hasSize(1);

        // when
        reservationRepository.deleteById(1L);

        // then
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).isEmpty();
    }
}
