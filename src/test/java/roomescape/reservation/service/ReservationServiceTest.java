package roomescape.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.dto.ReservationCreateInfo;
import roomescape.reservation.dto.ReservationIdResponse;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.schedule.model.Schedule;
import roomescape.theme.model.Theme;
import roomescape.user.model.Role;
import roomescape.user.model.User;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
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
        jdbcTemplate.update("INSERT INTO schedule (id, theme_id, start_at, end_at) VALUES (?, ?, ?, ?)",
                2L, 1L, "2026-12-10 15:00:00", "2026-12-10 17:00:00");
        reservationRepository.create(new Reservation(user, schedule, theme));
    }

    @Test
    void 데이터베이스에_저장된_모든_예약을_조회한다() {
        ReservationsResponse response = reservationService.findAll();

        assertThat(response.getReservationsResponse()).hasSize(1);
    }

    @Test
    void 새로운_예약을_생성한다() {
        ReservationCreateInfo info = new ReservationCreateInfo(1L,
                LocalDateTime.of(2026, 12, 10, 15, 0), 1L);

        ReservationIdResponse response = reservationService.create(info);

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

    @Test
    void 존재하지_않는_테마로_예약을_시도하면_예외가_발생한다() {
        ReservationCreateInfo info = new ReservationCreateInfo(1L,
                LocalDateTime.of(2026, 12, 10, 15, 0), 999L);

        assertThatThrownBy(() -> reservationService.create(info))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테마입니다.");
    }

    @Test
    void 등록된_스케줄이_없는_시간에_예약을_시도하면_예외가_발생한다() {
        ReservationCreateInfo info = new ReservationCreateInfo(1L,
                LocalDateTime.of(2026, 12, 10, 10, 0), 1L);

        assertThatThrownBy(() -> reservationService.create(info))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록된 스케줄이 없습니다.");
    }

    @Test
    void 이미_예약이_완료된_스케줄에_중복_예약을_시도하면_예외가_발생한다() {
        ReservationCreateInfo info = new ReservationCreateInfo(1L,
                LocalDateTime.of(2026, 12, 10, 12, 0), 1L); 

        assertThatThrownBy(() -> reservationService.create(info))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간은 이미 예약이 완료되었습니다.");
    }
}
