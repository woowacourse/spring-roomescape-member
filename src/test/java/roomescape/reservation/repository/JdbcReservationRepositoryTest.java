package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;

@JdbcTest
class JdbcReservationRepositoryTest {

    private ReservationRepository reservationRepository;
    private Long setupTimeId;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationRepository = new JdbcReservationRepository(jdbcTemplate);
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        setupTimeId = jdbcTemplate.queryForObject("SELECT id FROM reservation_time WHERE start_at = ?", Long.class, "10:00");
    }

    @Test
    @DisplayName("예약을 저장하고 반환된 객체의 ID를 확인한다.")
    void saveTest() {
        // given
        ReservationTime time = new ReservationTime(setupTimeId, LocalTime.of(10, 0));
        Reservation reservation = new Reservation(null, "브라운", LocalDate.of(2024, 5, 1), time);

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
        Reservation saved = reservationRepository.save(new Reservation(null, "브라운", LocalDate.of(2024, 5, 1), time));

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

        // when
        reservationRepository.save(new Reservation(null, "브라운", LocalDate.of(2024, 5, 1), time));
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations).hasSize(1);
        assertThat(reservations.get(0).getTime().getStartAt()).isEqualTo(startTime);
    }

    @Test
    @DisplayName("특정 날짜와 시간 ID로 예약 존재 여부를 확인한다.")
    void existsByDateAndTimeIdTest() {
        // given
        ReservationTime time = new ReservationTime(setupTimeId, LocalTime.of(10, 0));
        LocalDate date = LocalDate.of(2024, 5, 1);
        reservationRepository.save(new Reservation(null, "브라운", date, time));

        // when
        boolean exists = reservationRepository.existsByDateAndTimeId(date, setupTimeId);
        boolean notExists = reservationRepository.existsByDateAndTimeId(date, 999L);

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
