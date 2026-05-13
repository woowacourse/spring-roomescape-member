package roomescape.repository.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.dto.ReservationTimesWithStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql("/clear.sql")
class JdbcReservationRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    JdbcReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new JdbcReservationRepository(jdbcTemplate);
    }

    @Test
    void 예약을_저장하고_조회한다() {
        insertReservationTime("10:00", "10:30");
        insertTheme("링", "공포 테마", "http:~");
        Reservation reservation = Reservation.create(
                "브라운",
                LocalDate.of(2026, 8, 5),
                ReservationTime.of(1L, LocalTime.of(10, 0), LocalTime.of(10, 30)),
                Theme.of(1L, "링", "공포 테마", "http:~"),
                LocalDateTime.of(2026, 8, 5, 9, 0)
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).hasSize(1);
        assertThat(reservations.getFirst().getId()).isEqualTo(savedReservation.getId());
        assertThat(reservations.getFirst().getCustomerName()).isEqualTo("브라운");
        assertThat(reservations.getFirst().getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));
        assertThat(reservations.getFirst().getTheme().getName()).isEqualTo("링");
    }

    @Test
    void 예약을_id로_조회한다() {
        insertReservationTime("10:00", "10:30");
        insertTheme("링", "공포 테마", "http:~");
        insertReservation("브라운", "2026-08-05", 1L, 1L);

        Optional<Reservation> reservation = reservationRepository.findById(1L);

        assertThat(reservation).isPresent();
        assertThat(reservation.get().getCustomerName()).isEqualTo("브라운");
    }

    @Test
    void 예약을_삭제한다() {
        insertReservationTime("10:00", "10:30");
        insertTheme("링", "공포 테마", "http:~");
        insertReservation("브라운", "2026-08-05", 1L, 1L);

        reservationRepository.deleteById(1L);

        assertThat(reservationRepository.findAll()).isEmpty();
    }

    @Test
    void 존재하지_않는_예약을_삭제해도_예외가_발생하지_않는다() {
        reservationRepository.deleteById(1L);

        assertThat(reservationRepository.findAll()).isEmpty();
    }

    @Test
    void 날짜와_테마에_따른_예약_시간_상태를_조회한다() {
        insertReservationTime("10:00", "10:30");
        insertReservationTime("11:00", "11:30");
        insertTheme("링", "공포 테마", "http:~");
        insertReservation("브라운", "2026-08-05", 1L, 1L);

        List<ReservationTimesWithStatus> timeStatuses = reservationRepository.findReservationTimeStatusesByDateAndThemeId(
                LocalDate.of(2026, 8, 5),
                1L
        );

        assertThat(timeStatuses).hasSize(2);
        assertThat(timeStatuses.get(0).id()).isEqualTo(1L);
        assertThat(timeStatuses.get(0).reserved()).isTrue();
        assertThat(timeStatuses.get(1).id()).isEqualTo(2L);
        assertThat(timeStatuses.get(1).reserved()).isFalse();
    }


    private void insertReservationTime(final String startAt, final String endAt) {
        jdbcTemplate.update(
                "INSERT INTO reservation_time (start_at, end_at) VALUES (?, ?)",
                startAt,
                endAt
        );
    }

    private void insertTheme(final String name, final String description, final String thumbnailUrl) {
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)",
                name,
                description,
                thumbnailUrl
        );
    }

    private void insertReservation(final String name, final String date, final Long timeId, final Long themeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                name,
                date,
                timeId,
                themeId
        );
    }
}
