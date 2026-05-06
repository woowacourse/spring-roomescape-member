package roomescape.reservation.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class JdbcReservationRepositoryTest {

    private ReservationRepository reservationRepository;
    private Long setupTimeId;
    private Long setupThemeId;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationRepository = new JdbcReservationRepository(jdbcTemplate);

        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        setupTimeId = jdbcTemplate.queryForObject("SELECT id FROM reservation_time WHERE start_at = ?", Long.class, "10:00");

        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)", "우테코", "우테코 전용 테마", "https://example.com");
        setupThemeId = jdbcTemplate.queryForObject("SELECT id FROM theme WHERE name = ?", Long.class, "우테코");
    }

    @Test
    @DisplayName("예약을 저장하고 반환된 객체의 ID를 확인한다.")
    void saveTest() {
        // given
        ReservationTime time = new ReservationTime(setupTimeId, LocalTime.of(10, 0));
        Theme theme = new Theme(setupThemeId, "우테코", "우테코 전용 테마", "https://example.com");

        Reservation reservation = new Reservation(null, "브라운", LocalDate.of(2024, 5, 1), time, theme);

        // when
        Reservation saved = reservationRepository.save(reservation);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("브라운");
    }

    @Test
    @DisplayName("ID를 통해 예약을 삭제한다.")
    void deleteByIdTest() {
        // given
        ReservationTime time = new ReservationTime(setupTimeId, LocalTime.of(10, 0));
        Theme theme = new Theme(setupThemeId, "우테코", "우테코 전용 테마", "https://example.com");

        Reservation saved = reservationRepository.save(new Reservation(null, "브라운", LocalDate.of(2024, 5, 1), time, theme));

        // when
        reservationRepository.deleteById(saved.getId());

        // then
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).isEmpty();
    }

    @Test
    @DisplayName("모든 예약 목록을 조회한다. (Join 확인)")
    void findAllTest() {
        // given
        LocalTime startTime = LocalTime.of(10, 0);
        ReservationTime time = new ReservationTime(setupTimeId, startTime);
        Theme theme = new Theme(setupThemeId, "우테코", "우테코 전용 테마", "https://example.com");

        // when
        reservationRepository.save(new Reservation(null, "브라운", LocalDate.of(2024, 5, 1), time, theme));
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations).hasSize(1);
        assertThat(reservations.get(0).getTime().getStartAt()).isEqualTo(startTime);
    }

    @Test
    @DisplayName("특정 날짜와 시간 ID로 예약 존재 여부를 확인한다.")
    void existsByDateAndTimeIdAndThemeIdTest() {
        // given
        ReservationTime time = new ReservationTime(setupTimeId, LocalTime.of(10, 0));
        LocalDate date = LocalDate.of(2024, 5, 1);
        Theme theme = new Theme(setupThemeId, "우테코", "우테코 전용 테마", "https://example.com");

        reservationRepository.save(new Reservation(null, "브라운", date, time, theme));

        // when
        boolean exists = reservationRepository.existsByDateAndTimeIdAndThemeId(date, setupTimeId, theme.getId());
        boolean notExists = reservationRepository.existsByDateAndTimeIdAndThemeId(date, 999L, theme.getId());

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
