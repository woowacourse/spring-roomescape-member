package roomescape.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.dto.ReservationIdResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.schedule.model.Schedule;
import roomescape.schedule.repository.ScheduleRepository;
import roomescape.theme.model.Theme;
import roomescape.user.model.Role;
import roomescape.user.model.User;
import roomescape.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    private final User user = new User(1L, "user1", Role.USER);
    private final Theme theme = new Theme(1L, "공포", "설명", "경로", LocalTime.of(2, 0));
    private final Schedule schedule = new Schedule(1L, LocalDateTime.of(2026, 12, 10, 12, 0), theme);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository; // 유저 ID 조회를 위해 주입

    @Autowired
    private ScheduleRepository scheduleRepository; // 스케줄 ID 조회를 위해 주입

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO \"USER\" (id, name, role) VALUES (?, ?, ?)",
                1L, "user1", "USER");
        jdbcTemplate.update("INSERT INTO theme (id, name, description, image_url, required_time) VALUES (?, ?, ?, ?, ?)",
                1L, "공포", "설명", "경로", LocalTime.of(2, 0));
        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                1L, 1L, "2026-12-10 12:00:00", "2026-12-10 14:00:00");
        reservationRepository.create(new Reservation(user, schedule, theme));
    }

    @Test
    void 데이터베이스에_저장된_모든_예약을_조회한다() {
        ReservationsResponse response = reservationService.findAll();

        assertThat(response.getReservationsResponse()).hasSize(1);
    }

    @Test
    void 새로운_예약을_생성한다() {
        User user = userRepository.findById(1L);
        Schedule schedule = scheduleRepository.findById(1L);

        ReservationRequest request = new ReservationRequest(user.getId(), schedule.getId());

        // 2. When
        ReservationIdResponse response = reservationService.create(request);

        // 3. Then
        assertThat(response.getId()).isNotNull();
        assertThat(reservationService.findAll().getReservationsResponse()).hasSize(2);
    }

    @Test
    void 예약을_삭제한다() {
        Long targetId = reservationService.findAll()
                .getReservationsResponse()
                .get(0)
                .getReservationId();

        reservationService.delete(targetId);

        assertThat(reservationService.findAll().getReservationsResponse()).isEmpty();
    }
}
