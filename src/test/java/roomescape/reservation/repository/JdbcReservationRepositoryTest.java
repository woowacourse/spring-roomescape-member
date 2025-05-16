package roomescape.reservation.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.entity.Reservation;
import roomescape.time.entity.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@JdbcTest
class JdbcReservationRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setup() {
        reservationRepository = new JdbcReservationRepository(jdbcTemplate);
        jdbcTemplate.update("INSERT INTO member(id, name, email, password, role) VALUES ( ?, ?, ?, ?, ? )", 1L, "test", "email", "password", "USER");
    }

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time(id, start_at) VALUES (?, ?)", 1, "10:00");
        jdbcTemplate.update("INSERT INTO theme(id, name, description, thumbnail) VALUES (?, ?, ?, ?)", 1, "hello", "hi", "thumbnail");

        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Reservation reservation = new Reservation(1L, 1L, LocalDate.of(2025, 1, 2), time, 1L);

        // when
        reservationRepository.save(reservation);

        // then
        assertThat(reservationRepository.findAll()).hasSize(1);
    }

    @DisplayName("삭제 테스트")
    @Test
    void deleteTest() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", 1, "10:00");
        jdbcTemplate.update("INSERT INTO theme(id, name, description, thumbnail) VALUES (?, ?, ?, ?)", 1, "hello", "hi", "thumbnail");
        jdbcTemplate.update("INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?, ?)", 1, 1, "2025-01-01", 1, 1);

        // when
        reservationRepository.deleteById(1L);

        // then
        assertThat(reservationRepository.findAll()).hasSize(0);
    }

    @DisplayName("time id로 조회할 수 있다.")
    @Test
    void findByIdTest() {
        // given
        // time
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", 1, "10:00");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES ( ?, ? )", 2, "12:00");
        // theme
        jdbcTemplate.update("INSERT INTO theme (id, name, description, thumbnail) VALUES ( ?, ?, ?, ? )", 1, "hello", "hi", "hh");
        // reservation
        jdbcTemplate.update("INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( ?, ?, ?, ?, ? )", 1, 1, "2025-01-01", 1, 1);
        jdbcTemplate.update("INSERT INTO reservation (id, member_id, date, time_id, theme_id) VALUES ( ?, ?, ?, ?, ? )", 2, 1, "2025-01-01", 2, 1);

        // when
        List<Reservation> firstTimeReservations = reservationRepository.findAllByTimeId(1L);
        List<Reservation> secondTimeReservations = reservationRepository.findAllByTimeId(2L);

        // then
        assertSoftly((softly) -> {
            softly.assertThat(firstTimeReservations).hasSize(1);
            softly.assertThat(firstTimeReservations.getFirst().getId()).isEqualTo(1L);
            softly.assertThat(secondTimeReservations).hasSize(1);
            softly.assertThat(secondTimeReservations.getFirst().getId()).isEqualTo(2L);        });
    }
}
