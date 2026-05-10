package roomescape.reservation.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired; // JdbcTemplate 주입을 위해 유지
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest; // SpringBootTest 대신 사용
import org.springframework.jdbc.core.JdbcTemplate; // JdbcTemplate은 그대로 사용
import roomescape.reservation.model.Reservation;
import roomescape.schedule.model.Schedule;
import roomescape.theme.model.Theme;
import roomescape.user.model.Role;
import roomescape.user.model.User;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class ReservationRepositoryTest {

    private final User user = new User(1L, "user1", Role.USER);
    private final Theme theme = new Theme(1L, "공포", "설명", "경로", LocalTime.of(2, 0));
    private final Schedule schedule = new Schedule(1L, LocalDateTime.of(2026, 12, 10, 12, 0), theme);

    private ReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationRepository(jdbcTemplate);

        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM schedule");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM \"USER\"");

        jdbcTemplate.update("INSERT INTO \"USER\" (id, name, role) VALUES (?, ?, ?)",
                1L, "user1", "USER");
        jdbcTemplate.update("INSERT INTO theme (id, name, description, image_url, required_time) VALUES (?, ?, ?, ?, ?)",
                1L, "공포", "설명", "경로", LocalTime.of(2, 0));
        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                1L, 1L, "2026-12-10 12:00:00", "2026-12-10 14:00:00");
    }

    @Test
    void 예약을_데이터베이스에_성공적으로_저장하고_생성된_ID를_반환한다() {
        Reservation reservation = new Reservation(user, schedule);

        Long savedId = reservationRepository.create(reservation);

        assertThat(savedId).isNotNull();

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation WHERE id = ?", Integer.class, savedId);

        assertThat(count).isEqualTo(1);
    }

    @Test
    void 예약을_데이터베이스에서_정상적으로_조회한다() {
        Reservation reservation = new Reservation(user, schedule);

        reservationRepository.create(reservation);

        List<Reservation> reservations = reservationRepository.findAll();

        assertThat(reservations).isNotNull();
        assertThat(reservations.size()).isEqualTo(1);
        assertThat(reservations.getFirst().getUser().getName()).isEqualTo(user.getName());
        assertThat(reservations.getFirst().getSchedule().getStartAt()).isEqualTo(schedule.getStartAt());
        assertThat(reservations.getFirst().getSchedule().getTheme().getName()).isEqualTo(theme.getName());
    }

    @Test
    void 예약을_데이터베이스에서_정상적으로_삭제한다() {
        Reservation reservation = new Reservation(user, schedule);
        Long savedId = reservationRepository.create(reservation);

        reservationRepository.delete(savedId);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation WHERE id = ?", Integer.class, savedId);

        assertThat(count).isEqualTo(0);
    }
}
