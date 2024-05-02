package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Time time;
    private Theme theme;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        time = timeRepository.save(new Time(LocalTime.of(17, 30)));
        theme = themeRepository.save(new Theme("테마명", "설명", "썸네일URL"));
        reservation = new Reservation("브라운", LocalDate.of(2024, 4, 25), time, theme);
    }

    @Test
    @DisplayName("repository를 통해 조회한 예약 수는 DB를 통해 조회한 예약 수와 같다.")
    void readDbReservations() {
        reservationRepository.save(reservation);

        List<Reservation> reservations = reservationRepository.findAll();
        int count = JdbcTestUtils.countRowsInTable(jdbcTemplate, "reservation");

        assertThat(reservations.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("하나의 예약만 등록한 경우, DB를 조회 했을 때 조회 결과 개수는 1개이다.")
    void postReservationIntoDb() {
        reservationRepository.save(reservation);

        int count = JdbcTestUtils.countRowsInTable(jdbcTemplate, "reservation");
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("하나의 예약만 등록한 경우, 예약 삭제 뒤 DB를 조회 했을 때 조회 결과 개수는 0개이다.")
    void readReservationsSizeFromDbAfterPostAndDelete() {
        Reservation saved = reservationRepository.save(reservation);

        // TODO: count 쿼리를 날리는 대신 repository에서 삭제 시 반환하는 count 값을 사용하도록 변경
        reservationRepository.delete(saved.getId());
        int count = JdbcTestUtils.countRowsInTable(jdbcTemplate, "reservation");

        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("테마ID와 날짜를 통해 예약정보를 조회한다.")
    void readReservationsByThemeIdAndDate() {
        Time time = timeRepository.save(new Time(LocalTime.of(17, 30)));
        Theme theme = themeRepository.save(new Theme("테마명", "설명", "썸네일URL"));
        Reservation reservation = new Reservation("브라운", LocalDate.of(2024, 4, 25), time, theme);

        Reservation savedReservation = reservationRepository.save(reservation);
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(LocalDate.of(2024, 4, 25),
                theme.getId());

        assertThat(reservations.size()).isEqualTo(1);
        assertThat(reservations.get(0).getId()).isEqualTo(savedReservation.getId());
    }
}
