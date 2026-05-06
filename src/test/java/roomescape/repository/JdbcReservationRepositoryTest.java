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
import org.springframework.test.context.TestExecutionListeners;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;
import roomescape.testexecutionlistener.TestDatabaseInitializer;
import roomescape.testexecutionlistener.TestPortInitializer;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.JdbcThemeRepository;

@RommescapeRepositoryTest
class JdbcReservationRepositoryTest {

    private JdbcReservationRepository jdbcReservationRepository;
    private JdbcReservationTimeRepository jdbcReservationTimeRepository;
    private JdbcThemeRepository jdbcThemeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
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

        ReservationTime reservationTime = jdbcReservationTimeRepository.save(ReservationTime.createNew(time, theme));

        Reservation reservation = Reservation.createNew(name, date, reservationTime);
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

        ReservationTime reservationTime = jdbcReservationTimeRepository.save(ReservationTime.createNew(time, theme));

        // when & then
        assertThrows(DataIntegrityViolationException.class, () -> {
            jdbcReservationRepository.save(
                    Reservation.createNew("쿠다", date, reservationTime)
            );
            jdbcReservationRepository.save(
                    Reservation.createNew("아루", date, reservationTime)
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

        ReservationTime reservationTime = jdbcReservationTimeRepository.save(ReservationTime.createNew(time, theme));
        jdbcReservationRepository.save(Reservation.createNew("쿠다", date, reservationTime));

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

        ReservationTime reservationTime = jdbcReservationTimeRepository.save(ReservationTime.createNew(time, theme));

        jdbcReservationRepository.save(Reservation.createNew("쿠다", date, reservationTime));

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

    @Test
    @DisplayName("날짜와 테마로 예약된 시간 ID를 조회한다")
    void findAllByDateAndThemeId_test() {
        // given
        LocalDate date = LocalDate.parse("2026-08-06");
        Theme firstTheme = createTheme("우테코의 밤");
        Theme secondTheme = createTheme("우테코 연구소");

        ReservationTime firstThemeFirstTime = jdbcReservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.parse("10:00"), firstTheme)
        );
        ReservationTime firstThemeSecondTime = jdbcReservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.parse("11:00"), firstTheme)
        );
        ReservationTime secondThemeTime = jdbcReservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.parse("10:00"), secondTheme)
        );

        jdbcReservationRepository.save(Reservation.createNew("쿠다", date, firstThemeFirstTime));
        jdbcReservationRepository.save(Reservation.createNew("아루", date, firstThemeSecondTime));
        jdbcReservationRepository.save(Reservation.createNew("도기", date.plusDays(1), firstThemeFirstTime));
        jdbcReservationRepository.save(Reservation.createNew("포비", date, secondThemeTime));

        // when
        List<Long> reservedTimeIds = jdbcReservationRepository.findAllByDateAndThemeId(date, firstTheme.getId());

        // then
        assertThat(reservedTimeIds)
                .containsExactlyInAnyOrder(firstThemeFirstTime.getId(), firstThemeSecondTime.getId());
    }

    @Test
    @DisplayName("최근 기간 기준 인기 테마를 예약 수 순서대로 조회한다")
    void findPopularThemes_test() {
        // given
        LocalDate today = LocalDate.now();
        Theme firstTheme = createTheme("미술관의 밤");
        Theme secondTheme = createTheme("심해 연구소");
        Theme thirdTheme = createTheme("폐병원 탈출");

        ReservationTime firstThemeTime = jdbcReservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.parse("10:00"), firstTheme)
        );
        ReservationTime secondThemeTime = jdbcReservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.parse("11:00"), secondTheme)
        );
        ReservationTime thirdThemeTime = jdbcReservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.parse("12:00"), thirdTheme)
        );

        jdbcReservationRepository.save(Reservation.createNew("쿠다", today.minusDays(1), firstThemeTime));
        jdbcReservationRepository.save(Reservation.createNew("아루", today.minusDays(2), firstThemeTime));
        jdbcReservationRepository.save(Reservation.createNew("도기", today.minusDays(3), firstThemeTime));

        jdbcReservationRepository.save(Reservation.createNew("포비", today.minusDays(1), secondThemeTime));
        jdbcReservationRepository.save(Reservation.createNew("솔라", today.minusDays(2), secondThemeTime));

        jdbcReservationRepository.save(Reservation.createNew("레오", today.minusDays(1), thirdThemeTime));
        jdbcReservationRepository.save(Reservation.createNew("오래된예약", today.minusDays(10), thirdThemeTime));

        // when
        List<Theme> popularThemes = jdbcReservationRepository.findPopularThemes(7, 2, LocalDate.parse("2026-05-06"));

        // then
        assertThat(popularThemes).hasSize(2);
        assertThat(popularThemes.get(0).getId()).isEqualTo(firstTheme.getId());
        assertThat(popularThemes.get(1).getId()).isEqualTo(secondTheme.getId());
    }

    private Theme createTheme(final String name) {
        return jdbcThemeRepository.save(
                Theme.createNew(name, "추리 테마", "https://example.com/theme.png")
        );
    }

}
