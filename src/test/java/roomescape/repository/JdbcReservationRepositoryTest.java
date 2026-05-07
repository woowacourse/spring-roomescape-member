package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.JdbcThemeRepository;

@JdbcTest
class JdbcReservationRepositoryTest {

    private JdbcReservationRepository jdbcReservationRepository;
    private JdbcReservationTimeRepository jdbcReservationTimeRepository;
    private JdbcThemeRepository jdbcThemeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        clearTables();
        jdbcReservationRepository = new JdbcReservationRepository(jdbcTemplate);
        jdbcReservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        jdbcThemeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("예약 저장")
    void reservation_save_test() {
        //given
        String name = "쿠다";
        LocalDate date = LocalDate.parse("2023-08-05");
        LocalTime time = LocalTime.parse("10:00");
        Theme theme = createTheme("미술관의 밤");

        ReservationTime reservationTime = jdbcReservationTimeRepository.save(ReservationTime.createNew(time));

        Reservation reservation = Reservation.createNew(name, date, theme, reservationTime);
        //when
        Reservation result = jdbcReservationRepository.save(reservation);
        Reservation saved = jdbcReservationRepository.findById(result.getId())
                .orElseThrow();

        // then
        assertThat(result).isEqualTo(saved);
    }

    @Test
    @DisplayName("예약 저장 중복 예외")
    void reservation_save_duplicate_test() {
        // given
        LocalDate date = LocalDate.parse("2026-08-06");
        LocalTime time = LocalTime.parse("10:00");
        Theme theme = createTheme("미술관의 밤");

        ReservationTime reservationTime = jdbcReservationTimeRepository.save(ReservationTime.createNew(time));

        // when & then
        assertThrows(DataIntegrityViolationException.class, () -> {
            jdbcReservationRepository.save(
                    Reservation.createNew("쿠다", date, theme, reservationTime)
            );
            jdbcReservationRepository.save(
                    Reservation.createNew("아루", date, theme, reservationTime)
            );
        });
    }

    @Test
    @DisplayName("예약 전체 조회")
    void reservation_findAll_test() {
        //given
        LocalDate date = LocalDate.parse("2026-08-06");
        LocalTime time = LocalTime.parse("10:00");
        Theme theme = createTheme("미술관의 밤");

        ReservationTime reservationTime = jdbcReservationTimeRepository.save(ReservationTime.createNew(time));
        jdbcReservationRepository.save(Reservation.createNew("쿠다", date, theme, reservationTime));

        //when
        List<Reservation> reservations = jdbcReservationRepository.findAll();

        //then
        assertThat(reservations.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 삭제")
    void reservation_delete_test() {
        // given
        LocalDate date = LocalDate.parse("2026-08-06");
        LocalTime time = LocalTime.parse("10:00");
        Theme theme = createTheme("미술관의 밤");

        ReservationTime reservationTime = jdbcReservationTimeRepository.save(ReservationTime.createNew(time));

        jdbcReservationRepository.save(Reservation.createNew("쿠다", date, theme, reservationTime));

        int beforeSize = jdbcReservationRepository.findAll().size();

        Reservation reservation = jdbcReservationRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        // when
        jdbcReservationRepository.deleteById(reservation.getId());

        // then
        int afterSize = jdbcReservationRepository.findAll().size();

        assertThat(afterSize).isEqualTo(beforeSize - 1);
    }

    private void clearTables() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    private Theme createTheme(final String name) {
        return jdbcThemeRepository.save(
                Theme.createNew(name, "추리 테마", "https://example.com/theme.png")
        );
    }

}
