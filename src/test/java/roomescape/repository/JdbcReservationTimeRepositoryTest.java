package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
class JdbcReservationTimeRepositoryTest {

    private JdbcReservationTimeRepository jdbcReservationTimeRepository;
    private JdbcReservationRepository jdbcReservationRepository;
    private JdbcThemeRepository jdbcThemeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        clearTables();
        jdbcReservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        jdbcReservationRepository = new JdbcReservationRepository(jdbcTemplate);
        jdbcThemeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("예약 시간 전체 조회")
    void reservationTime_findAll_test() {
        //given & when
        LocalTime time = LocalTime.parse("11:00");

        ReservationTime nonIdReservationTime = ReservationTime.createNew(time);
        jdbcReservationTimeRepository.save(nonIdReservationTime);

        Optional<ReservationTime> reservationTime = jdbcReservationTimeRepository.findAll()
                .stream()
                .findFirst();
        //then
        assertThat(reservationTime).isNotEmpty();
    }

    @Test
    @DisplayName("예약 시간 저장")
    void reservationTime_save_test() {
        //given
        LocalTime time = LocalTime.parse("11:00");
        ReservationTime nonIdReservationTime = ReservationTime.createNew(time);

        //when
        ReservationTime result = jdbcReservationTimeRepository.save(nonIdReservationTime);
        ReservationTime saved = jdbcReservationTimeRepository.findById(result.getId())
                .orElseThrow();
        //then
        assertThat(result).isEqualTo(saved);
    }

    @Test
    @DisplayName("예약 시간 저장 예외")
    void save_duplicate_test() {
        //given
        LocalTime time = LocalTime.parse("11:00");

        //when
        jdbcReservationTimeRepository.save(ReservationTime.createNew(time));

        //then
        assertThrows(DataIntegrityViolationException.class, () -> {
            jdbcReservationTimeRepository.save(ReservationTime.createNew(time));
        });
    }

    @Test
    @DisplayName("예약 시간 삭제")
    void reservationTime_delete_test() {
        // given
        LocalTime time = LocalTime.parse("11:00");
        ReservationTime nonIdReservationTime = ReservationTime.createNew(time);
        ReservationTime reservationTime = jdbcReservationTimeRepository.save(nonIdReservationTime);
        int beforeSize = jdbcReservationTimeRepository.findAll().size();

        // when
        jdbcReservationTimeRepository.deleteById(reservationTime.getId());

        // then
        int afterSize = jdbcReservationTimeRepository.findAll().size();

        assertThat(afterSize).isEqualTo(beforeSize - 1);
    }

    @Test
    @DisplayName("날짜와 테마 기준 예약 가능한 시간을 조회한다")
    void findAvailableTimes_test() {
        LocalDate date = LocalDate.parse("2026-08-06");
        Theme firstTheme = createTheme("미술관의 밤");
        Theme secondTheme = createTheme("심해 연구소");

        ReservationTime ten = jdbcReservationTimeRepository.save(ReservationTime.createNew(LocalTime.parse("10:00")));
        ReservationTime eleven = jdbcReservationTimeRepository.save(ReservationTime.createNew(LocalTime.parse("11:00")));

        jdbcReservationRepository.save(Reservation.createNew("쿠다", date, firstTheme, ten));
        jdbcReservationRepository.save(Reservation.createNew("포비", date, secondTheme, ten));

        List<ReservationTime> availableTimes = jdbcReservationTimeRepository.findAvailableTimes(date, firstTheme.getId());

        assertThat(availableTimes)
                .extracting(ReservationTime::getId)
                .containsExactly(eleven.getId());
    }

    private void clearTables() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    private Theme createTheme(final String name) {
        return jdbcThemeRepository.save(
                Theme.createNew(name, "추리 테마", "https://example.com/theme.png")
        );
    }

}
