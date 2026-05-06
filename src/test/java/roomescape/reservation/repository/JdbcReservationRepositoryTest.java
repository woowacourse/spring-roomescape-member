package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void findTimeIdsByDate() {
        Long timeId1 = insertTime("10:00", "12:00");
        Long timeId2 = insertTime("13:00", "15:00");

        LocalDate date = LocalDate.of(2026, 5, 6);
        insertReservation("윤호준", date, timeId1);
        insertReservation("박다혜", date, timeId2);

        assertThat(reservationRepository.findTimeIdsByDate(date))
                .containsExactly(timeId1, timeId2);

        assertThat(reservationRepository.findTimeIdsByDate(date.plusDays(1)))
                .isEmpty();
    }

    private Long insertTime(String startAt, String endAt) {
        jdbcTemplate.update(
                "INSERT INTO reservation_time (start_time, end_time) VALUES (?, ?)",
                startAt,
                endAt
        );
        return jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_time = ?",
                Long.class,
                startAt
        );
    }

    private void insertReservation(String name, LocalDate date, Long timeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)",
                name,
                Date.valueOf(date),
                timeId
        );
    }
}

