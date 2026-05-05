package roomescape.reservation.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.model.Reservation;
import roomescape.schedule.model.Schedule;
import roomescape.theme.model.Theme;
import roomescape.user.model.Role;
import roomescape.user.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ReservationRepositoryTest {

    private final User user = new User(1L, "user1", Role.USER);
    private final Theme theme = new Theme(1L, "공포", "설명", "경로");
    private final Schedule schedule = new Schedule(1L, LocalDateTime.of(2026, 12, 10, 12, 0),
            LocalDateTime.of(2026, 12, 10, 14, 0), theme);

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO \"USER\" (id, name, role) VALUES (?, ?, ?)",
                1L, "user1", "USER");
        jdbcTemplate.update("INSERT INTO theme (id, name, description, image_url) VALUES (?, ?, ?, ?)",
                1L, "공포", "설명", "경로");
        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                1L, 1L, "2026-12-10 12:00:00", "2026-12-10 14:00:00");
    }

    @Test
    void 예약을_데이터베이스에_성공적으로_저장하고_생성된_ID를_반환한다() {
        Reservation reservation = new Reservation(user, schedule, theme);

        Long savedId = reservationRepository.create(reservation);

        assertThat(savedId).isNotNull();

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation WHERE id = ?", Integer.class, savedId);

        assertThat(count).isEqualTo(1);
    }

    @Test
    void 예약을_데이터베이스에서_정상적으로_조회한다() {
        Reservation reservation = new Reservation(user, schedule, theme);

        reservationRepository.create(reservation);

        List<Reservation> reservations = reservationRepository.findAll();

        assertThat(reservations).isNotNull();
        assertThat(reservations.size()).isEqualTo(1);
        assertThat(reservations.getFirst().getUser().getName()).isEqualTo(user.getName());
        assertThat(reservations.getFirst().getSchedule().getStartAt()).isEqualTo(schedule.getStartAt());
        assertThat(reservations.getFirst().getTheme().getName()).isEqualTo(theme.getName());
    }

    @Test
    void 예약을_데이터베이스에서_정상적으로_삭제한다() {
        Reservation reservation = new Reservation(user, schedule, theme);
        Long savedId = reservationRepository.create(reservation);

        reservationRepository.delete(savedId);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation WHERE id = ?", Integer.class, savedId);

        assertThat(count).isEqualTo(0);
    }
}
