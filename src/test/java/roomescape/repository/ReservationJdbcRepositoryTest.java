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
import roomescape.Fixtures;
import roomescape.model.Reservation;
import roomescape.model.Theme;
import roomescape.model.TimeSlot;

@JdbcTest
public class ReservationJdbcRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("타임 슬롯과 테마를 미리 세팅")
    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into RESERVATION_TIME (id, start_at) values (?, ?)", 1, "10:00");
        jdbcTemplate.update("insert into THEME (id, name, description, thumbnail) values (?, ?, ?, ?)",
            1,
            "레벨2 탈출",
            "우테코 레벨2를 탈출하는 내용입니다.",
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
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
            Fixtures.JUNK_TIME_SLOT,
            Fixtures.JUNK_THEME
        );
    }
}
