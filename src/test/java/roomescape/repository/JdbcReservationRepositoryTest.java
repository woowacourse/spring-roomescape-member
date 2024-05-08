package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;

@JdbcTest
class JdbcReservationRepositoryTest {

    private final JdbcTemplate jdbcTemplate;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public JdbcReservationRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationRepository = new JdbcReservationRepository(jdbcTemplate);
        this.reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        this.themeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("모든 예약들을 조회한다.")
    void findAll() {
        createReservation();

        List<Reservation> reservations = reservationRepository.findAll(memberId, themeId, dateFrom, dateTo);
        Reservation reservation = reservations.get(0);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(reservations).hasSize(1);
            softly.assertThat(reservation.getName()).isEqualTo("예약1");
            softly.assertThat(reservation.getDate()).isEqualTo("2024-05-04");
            softly.assertThat(reservation.getTime().getStartAt()).isEqualTo("10:00");
            softly.assertThat(reservation.getTheme().getName()).isEqualTo("테마1");
            softly.assertThat(reservation.getTheme().getDescription()).isEqualTo("테마1 설명");
            softly.assertThat(reservation.getTheme().getThumbnail()).isEqualTo("https://example1.com");
        });
    }

    @Test
    @DisplayName("예약을 생성한다.")
    void save() {
        createTheme();
        createReservationTime();

        LocalDate date = LocalDate.of(2024, 5, 4);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "테마1", "테마1 설명", "https://example1.com");
        reservationRepository.save(new Reservation(null, "예약1", date, time, theme));

        int count = JdbcTestUtils.countRowsInTable(jdbcTemplate, "reservation");
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteById() {
        createReservation();

        reservationRepository.deleteById(1L);

        int count = JdbcTestUtils.countRowsInTable(jdbcTemplate, "reservation");
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("id에 해당하는 예약이 존재하는지 확인한다.")
    void existsById() {
        createReservation();

        assertThat(reservationRepository.existsById(1L)).isTrue();
    }

    @Test
    @DisplayName("예약 시간 id에 해당하는 예약이 존재하는지 확인한다.")
    void existsByTimeId() {
        createReservation();

        assertThat(reservationRepository.existsByTimeId(1L)).isTrue();
    }

    @Test
    @DisplayName("테마 id에 해당하는 예약이 존재하는지 확인한다.")
    void existsByThemeId() {
        createReservation();

        assertThat(reservationRepository.existsByThemeId(1L)).isTrue();
    }

    @Test
    @DisplayName("예약이 존재하는지 확인한다.")
    void existsByReservation() {
        createReservation();

        LocalDate date = LocalDate.of(2024, 5, 4);
        assertThat(reservationRepository.existsByReservation(date, 1L, 1L)).isTrue();
    }

    private void createReservation() {
        createTheme();
        createReservationTime();
        jdbcTemplate.update("INSERT INTO reservation (id, name, date, time_id, theme_id) "
                + "VALUES (1, '예약1', '2024-05-04', 1, 1)");
    }

    private void createTheme() {
        jdbcTemplate.update("INSERT INTO theme (id, name, description, thumbnail) "
                + "VALUES (1, '테마1', '테마1 설명', 'https://example1.com')");
    }

    private void createReservationTime() {
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) "
                + "VALUES (1, '10:00')");
    }
}
