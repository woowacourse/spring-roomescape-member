package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.sql.Date;
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
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("save 후 생성된 id를 반환한다.")
    @Test
    void saveTest() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (1, '10:00')");

        Reservation reservation = Reservation.withoutId(
                "테스트행님",
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

                () -> assertThat(result.get("name")).isEqualTo("테스트행님"),
                () -> assertThat(result.get("date")).isEqualTo(Date.valueOf(LocalDate.of(2025, 4, 30))),
                () -> assertThat(result.get("time_id")).isEqualTo(1L)
        );
    }

    @DisplayName("모든 예약을 조회할 수 있다.")
    @Test
    void findAllTest() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (1, '10:00:00')");

        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES ('브라운', '2025-01-01', 1)");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES ('솔라', '2025-01-01', 1)");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES ('브리', '2025-01-01', 1)");

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
        assertThat(reservationRepository.findAll()).hasSize(0);
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (1, '10:00:00')");
        jdbcTemplate.update("INSERT INTO reservation (id, name, date, time_id) VALUES (1, '브라운', '2025-01-01', 1)");
        assertThat(reservationRepository.findAll()).hasSize(1);

        // when
        reservationRepository.deleteById(1L);

        // then
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).hasSize(0);
    }
}
