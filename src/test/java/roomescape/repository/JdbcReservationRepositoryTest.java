package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcReservationRepository jdbcReservationRepository;
    private TimeSlot savedTimeSlot;
    private Theme savedTheme;

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
                "CREATE TABLE IF NOT EXISTS reservation (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), date DATE, time_id BIGINT, theme_id BIGINT)");
        jdbcTemplate.execute("TRUNCATE TABLE reservation RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE time_slot RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE theme RESTART IDENTITY");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private void insertDependencyData() {
        JdbcTimeSlotRepository timeRepository = new JdbcTimeSlotRepository(jdbcTemplate);
        JdbcThemeRepository themeRepository = new JdbcThemeRepository(jdbcTemplate);
        savedTimeSlot = timeRepository.save(new TimeSlot(1L, LocalTime.of(10, 0)));
        savedTheme = themeRepository.save(new Theme(1L, "공포", "귀신의 집 탈출", "https://test.com"));
    }

    @Test
    @DisplayName("예약을 저장하고 영속화된 객체를 반환한다.")
    void save() {
        Reservation reservation = Reservation.transientOf("브라운", LocalDate.now(), savedTimeSlot, savedTheme);
        Reservation savedReservation = jdbcReservationRepository.save(reservation);
        assertThat(savedReservation.id()).isPositive();
    }

    @Test
    @DisplayName("식별자로 예약 객체를 조회한다.")
    void findById() {
        Reservation savedReservation = jdbcReservationRepository.save(Reservation.transientOf(
                "브라운",
                LocalDate.now(),
                savedTimeSlot,
                savedTheme
        ));
        Optional<Reservation> foundReservation = jdbcReservationRepository.findById(savedReservation.id());
        assertThat(foundReservation).isPresent();
        assertThat(foundReservation.get().name()).isEqualTo("브라운");
    }

    @Test
    @DisplayName("모든 예약 객체 목록을 조회한다.")
    void findAll() {
        jdbcReservationRepository.save(Reservation.transientOf(
                "브라운",
                LocalDate.now(), savedTimeSlot,
                savedTheme
        ));
        List<Reservation> reservations = jdbcReservationRepository.findAll();
        assertThat(reservations).hasSize(1);
    }

    @Test
    @DisplayName("식별자로 예약을 삭제한다.")
    void deleteById() {
        Reservation savedReservation = jdbcReservationRepository.save(Reservation.transientOf(
                "브라운",
                LocalDate.now(),
                savedTimeSlot,
                savedTheme
        ));
        jdbcReservationRepository.deleteById(savedReservation.id());
        assertThat(jdbcReservationRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("특정 날짜, 테마, 시간에 해당하는 예약이 이미 존재하면 true를 반환한다.")
    void existsByDateAndTimeIdAndThemeId() {
        Theme theme = savedTheme;
        Reservation reservation = Reservation.transientOf("브라운", LocalDate.now(), savedTimeSlot, theme);
        System.out.println(jdbcReservationRepository.findAll());
        System.out.println(jdbcReservationRepository.save(reservation));

        boolean exists = jdbcReservationRepository.existsByDateAndTimeIdAndThemeId(LocalDate.now(), savedTimeSlot.id(), 1L);
        assertThat(exists).isTrue();
    }
}
