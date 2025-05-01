package roomescape.reservation.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.entity.ReservationEntity;
import roomescape.time.entity.ReservationTimeEntity;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class JdbcReservationRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setup() {
        reservationRepository = new JdbcReservationRepository(jdbcTemplate);
    }

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time(id, start_at) VALUES (?, ?)", 1, "10:00");
        jdbcTemplate.update("INSERT INTO theme(id, name, description, thumbnail) VALUES (?, ?, ?, ?)", 1, "hello", "hi", "thumbnail");

        ReservationTimeEntity time = new ReservationTimeEntity(1L, LocalTime.of(10, 0));
        ReservationEntity reservation = new ReservationEntity(1L, "test", LocalDate.of(2025, 1, 2), time, 1L);

        // when
        reservationRepository.save(reservation);

        // then
        assertThat(reservationRepository.findAll()).hasSize(1);
    }
}
