package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.service.dto.request.ReservationTimeRequest;
import roomescape.service.dto.request.ThemeRequest;

@JdbcTest
class JdbcReservationRepositoryTest {

    private final JdbcTemplate jdbcTemplate;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    private Reservation savedReservation;

    @Autowired
    private JdbcReservationRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationRepository = new JdbcReservationRepository(jdbcTemplate);
        this.reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        this.themeRepository = new JdbcThemeRepository(jdbcTemplate);
    }


    @BeforeEach
    void saveReservation() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(10, 0));
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTimeRequest.toEntity());

        ThemeRequest themeRequest = new ThemeRequest("hi", "happy", "abcd.html");
        Theme theme = themeRepository.save(themeRequest.toEntity());

        Reservation reservation = new Reservation(null, "parang", LocalDate.of(2999, 3, 28), reservationTime, theme);
        savedReservation = reservationRepository.save(reservation);
    }

    @AfterEach
    void setUp() {
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN `id` RESTART");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN `id` RESTART");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN `id` RESTART");
    }

    @DisplayName("DB 예약 추가 테스트")
    @Test
    void save() {
        Assertions.assertThat(savedReservation.getId()).isEqualTo(1);
    }

    @DisplayName("DB 모든 예약 조회 테스트")
    @Test
    void findAllReservations() {
        List<Reservation> reservations = reservationRepository.findAllReservations();
        assertThat(reservations).hasSize(1);
    }

    @DisplayName("DB 예약 삭제 테스트")
    @Test
    void delete() {
        reservationRepository.delete(savedReservation.getId());
        List<Reservation> reservations = reservationRepository.findAllReservations();
        assertThat(reservations).isEmpty();
    }
}
