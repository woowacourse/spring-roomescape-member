package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcTemplateReservationRepository.class)
class JdbcTemplateReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private long timeId;

    @BeforeEach
    void setUp() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO reservation_time (start_at) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, "10:00");
            return ps;
        }, keyHolder);
        timeId = Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Test
    void 예약을_저장하면_id가_채워진_도메인을_반환한다() {
        ReservationTime time = new ReservationTime(timeId, LocalTime.of(10, 0));
        Reservation toSave = new Reservation(null, "브라운", LocalDate.of(2026, 5, 3), time);

        Reservation saved = reservationRepository.addReservation(toSave);

        assertThat(saved.id()).isNotNull();
        assertThat(saved.name()).isEqualTo("브라운");
        assertThat(saved.date()).isEqualTo(LocalDate.of(2026, 5, 3));
        assertThat(saved.time().id()).isEqualTo(timeId);
    }

    @Test
    void 모든_예약을_조인_조회한다() {
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)",
                "브라운", "2026-05-03", timeId);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)",
                "조이", "2026-05-04", timeId);

        List<Reservation> reservations = reservationRepository.findAllReservations();

        assertThat(reservations).hasSize(2);
        assertThat(reservations.get(0).time().startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 예약이_없으면_빈_리스트를_반환한다() {
        List<Reservation> reservations = reservationRepository.findAllReservations();

        assertThat(reservations).isEmpty();
    }

    @Test
    void id로_예약을_삭제한다() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, "브라운");
            ps.setString(2, "2026-05-03");
            ps.setLong(3, timeId);
            return ps;
        }, keyHolder);
        long reservationId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        reservationRepository.deleteById(reservationId);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM reservation", Integer.class);
        assertThat(count).isEqualTo(0);
    }
}
