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
import roomescape.support.DatabaseHelper;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ReservationRepositoryTest {

    private final User user = new User(1L, "user1", Role.USER);
    private final Theme theme = new Theme(1L, "공포", "설명", "경로", LocalTime.of(2, 0));
    private final Schedule schedule = new Schedule(1L, LocalDateTime.of(2026, 12, 10, 12, 0), theme);

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DatabaseHelper databaseHelper;

    @BeforeEach
    void setUp() {
        databaseHelper.cleanUp();
        databaseHelper.insertUser(1L, "user1", "USER");
        databaseHelper.insertTheme(1L, "공포", "설명", "경로", "02:00:00");
        databaseHelper.insertSchedule(1L, 1L, "2026-12-10 12:00:00", "2026-12-10 14:00:00");
    }

    @Test
    void 예약을_데이터베이스에_성공적으로_저장하고_생성된_ID를_반환한다() {
        Reservation reservation = new Reservation(user, schedule, theme);

        Reservation savedReservation = reservationRepository.create(reservation);
        Long savedId = savedReservation.getId();

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
        Reservation savedReservation = reservationRepository.create(reservation);
        Long savedId = savedReservation.getId();

        reservationRepository.delete(savedId);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation WHERE id = ?", Integer.class, savedId);

        assertThat(count).isEqualTo(0);
    }

    @Test
    void 사용자가_본인의_예약_목록을_정상적으로_조회한다() {
        Reservation reservation = new Reservation(user, schedule, theme);

        reservationRepository.create(reservation);

        List<Reservation> reservations = reservationRepository.findAllByUserId(user.getId());

        assertThat(reservations).isNotNull();
        assertThat(reservations.size()).isEqualTo(1);
        assertThat(reservations.getFirst().getUser().getName()).isEqualTo(user.getName());
        assertThat(reservations.getFirst().getSchedule().getStartAt()).isEqualTo(schedule.getStartAt());
        assertThat(reservations.getFirst().getTheme().getName()).isEqualTo(theme.getName());
        assertThat(reservations.getFirst().getUser().getId()).isEqualTo(user.getId());
    }
}
