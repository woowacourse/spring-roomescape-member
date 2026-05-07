package roomescape.repository;

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
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.TimeSlot;

@JdbcTest
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcReservationRepository jdbcReservationRepository;
    private TimeSlot savedTimeSlot;

    @BeforeEach
    void setUp() {
        jdbcReservationRepository = new JdbcReservationRepository(jdbcTemplate);
        executeSchema();
        insertDependencyData();
    }

    private void executeSchema() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS time_slot (id BIGINT AUTO_INCREMENT PRIMARY KEY, start_at TIME)");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS reservation (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), date DATE, time_id BIGINT)");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("TRUNCATE TABLE time_slot");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private void insertDependencyData() {
        JdbcTimeSlotRepository timeDao = new JdbcTimeSlotRepository(jdbcTemplate);
        savedTimeSlot = timeDao.save(TimeSlot.transientOf(LocalTime.of(10, 0)));
    }

    @Test
    @DisplayName("예약을 저장하고 영속화된 객체를 반환한다.")
    void save() {
        Reservation reservation = Reservation.transientOf("브라운", LocalDate.now(), savedTimeSlot,
                new Theme(1L, null, null, null));
        Reservation savedReservation = jdbcReservationRepository.save(reservation);
        assertThat(savedReservation.id()).isPositive();
    }

    @Test
    @DisplayName("식별자로 예약 객체를 조회한다.")
    void findById() {
        Reservation savedReservation = jdbcReservationRepository.save(Reservation.transientOf("브라운", LocalDate.now(),
                savedTimeSlot,
                new Theme(1L, null, null, null)));
        Reservation foundReservation = jdbcReservationRepository.findById(savedReservation.id());
        assertThat(foundReservation.name()).isEqualTo("브라운");
    }

    @Test
    @DisplayName("모든 예약 객체 목록을 조회한다.")
    void findAll() {
        jdbcReservationRepository.save(Reservation.transientOf("브라운", LocalDate.now(), savedTimeSlot,
                new Theme(1L, null, null, null)));
        List<Reservation> reservations = jdbcReservationRepository.findAll();
        assertThat(reservations).hasSize(1);
    }

    @Test
    @DisplayName("식별자로 예약을 삭제한다.")
    void deleteById() {
        Reservation savedReservation = jdbcReservationRepository.save(Reservation.transientOf("브라운", LocalDate.now(),
                savedTimeSlot,
                new Theme(1L, null, null, null)));
        jdbcReservationRepository.deleteById(savedReservation.id());
        assertThat(jdbcReservationRepository.findAll()).isEmpty();
    }
}
