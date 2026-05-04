package roomescape.reservation.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class JdbcReservationRepositoryTest {
    @Autowired
    private JdbcReservationRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    void 예약_저장_레포지토리_테스트() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "15:40");
        Long timeId = jdbcTemplate.queryForObject("SELECT id FROM reservation_time LIMIT 1", Long.class);

        Reservation savedReservation = repository.save("브라운", LocalDate.of(2023, 8, 5), timeId);
        Long id = jdbcTemplate.queryForObject("SELECT id FROM reservation LIMIT 1", Long.class);

        assertThat(savedReservation.getId()).isEqualTo(id);
        assertThat(savedReservation.getName()).isEqualTo("브라운");
        assertThat(savedReservation.getDate()).isEqualTo(LocalDate.of(2023, 8, 5));
        assertThat(savedReservation.getTime().getId()).isEqualTo(timeId);
        assertThat(savedReservation.getTime().getStartAt()).isEqualTo(LocalTime.of(15, 40));
    }

    @Test
    @Transactional
    void 전체_예약_조회_레포지토리_테스트() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "15:40");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "16:00");
        Long firstTimeId = jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_at = ?",
                Long.class,
                "15:40"
        );
        Long secondTimeId = jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_at = ?",
                Long.class,
                "16:00"
        );
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)", "브라운", "2023-08-05",
                firstTimeId);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)", "코니", "2023-08-05",
                secondTimeId);

        List<Reservation> reservations = repository.findAll();

        assertThat(reservations).hasSize(2);
        assertThat(reservations)
                .extracting(Reservation::getName)
                .containsExactly("브라운", "코니");
        assertThat(reservations)
                .extracting(Reservation::getDate)
                .containsExactly(LocalDate.of(2023, 8, 5), LocalDate.of(2023, 8, 5));
        assertThat(reservations)
                .extracting(reservation -> reservation.getTime().getId())
                .containsExactly(firstTimeId, secondTimeId);
        assertThat(reservations)
                .extracting(reservation -> reservation.getTime().getStartAt())
                .containsExactly(LocalTime.of(15, 40), LocalTime.of(16, 0));
    }

    @Test
    @Transactional
    void 예약_삭제_레포지토리_테스트(){
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "15:40");
        Long timeId = jdbcTemplate.queryForObject("SELECT id FROM reservation_time LIMIT 1", Long.class);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)", "브라운", "2023-08-05",
                timeId);
        Long id = jdbcTemplate.queryForObject("SELECT id FROM reservation LIMIT 1", Long.class);

        repository.deleteById(id);
        int rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);

        assertThat(rowCount).isEqualTo(0);
    }
}
