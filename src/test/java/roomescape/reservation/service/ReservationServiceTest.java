package roomescape.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.schedule.model.Schedule;
import roomescape.theme.model.Theme;
import roomescape.user.model.Role;
import roomescape.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    private final User user = new User(1L, "user1", Role.USER);
    private final Theme theme = new Theme(1L, "공포", "설명", "경로");
    private final Schedule schedule = new Schedule(1L, LocalDateTime.of(2026, 12, 10, 12, 0),
            LocalDateTime.of(2026, 12, 10, 14, 0), theme);

    @Autowired
    private ReservationService reservationService;

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
        reservationRepository.create(new Reservation(user, schedule, theme));
    }

    @Test
    void 데이터베이스에_저장된_모든_예약을_조회한다() {
        ReservationsResponse response = reservationService.findAll();

        assertThat(response.getReservationsResponse()).hasSize(1);
    }
}
