package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.model.Reservation;
import roomescape.model.TimeSlot;

@JdbcTest
public class ReservationJdbcRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long timeSlotId;

    @DisplayName("예약 시간을 미리 세팅")
    @BeforeEach
    void setUp() {
        var insert = new SimpleJdbcInsert(jdbcTemplate);
        var generatedId = insert
            .withTableName("RESERVATION_TIME")
            .usingGeneratedKeyColumns("id")
            .executeAndReturnKey(Map.of("start_at", "10:00"));
        timeSlotId = generatedId.longValue();
    }

    @Test
    @DisplayName("예약을 아이디로 조회한다.")
    void findReservation() {
        // given
        var repository = new ReservationJdbcRepository(jdbcTemplate);
        var reservation = readyReservation();
        var savedId = repository.save(reservation);

        // when
        Optional<Reservation> found = repository.findById(savedId);

        // then
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("예약을 저장한다.")
    void addReservation() {
        // given
        var repository = new ReservationJdbcRepository(jdbcTemplate);
        var reservation = readyReservation();

        // when
        repository.save(reservation);

        // then
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void removeReservation() {
        // given
        var repository = new ReservationJdbcRepository(jdbcTemplate);
        var reservation = readyReservation();
        var savedId = repository.save(reservation);

        // when
        repository.removeById(savedId);

        // then
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("모든 예약을 조회한다.")
    void getAllReservation() {
        // given
        var repository = new ReservationJdbcRepository(jdbcTemplate);
        var reservation1 = readyReservation();
        var reservation2 = readyReservation();
        repository.save(reservation1);
        repository.save(reservation2);

        // when & then
        assertThat(repository.findAll()).hasSize(2);
    }

    private Reservation readyReservation() {
        return Reservation.create(
            "브라운",
            LocalDate.of(2023, 12, 1),
            new TimeSlot(timeSlotId, LocalTime.of(10, 0))
        );
    }
}
