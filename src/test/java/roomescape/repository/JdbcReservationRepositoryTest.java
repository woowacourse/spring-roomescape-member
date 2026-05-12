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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.JdbcThemeRepository;

@RoomescapeRepositoryTest
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
    void reservation_save_success() {
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
    void reservation_save_whenDuplicate_throws() {
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
    void reservation_findAll_success() {
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
    void reservation_delete_success() {
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
    @DisplayName("예약 업데이트")
    void update_success() {
        // given
        LocalDate date = LocalDate.parse("2026-08-06");
        LocalTime time = LocalTime.parse("10:00");
        Theme theme = createTheme("미술관의 밤");

        ReservationTime reservationTime = jdbcReservationTimeRepository.save(ReservationTime.createNew(time, theme));

        Reservation reservation = jdbcReservationRepository.save(Reservation.createNew("쿠다", date, reservationTime));

        // when
        LocalDate newDate = date.plusDays(1);
        LocalTime newTime = LocalTime.parse("11:00");
        ReservationTime newReservationTime = jdbcReservationTimeRepository.save(
                ReservationTime.createNew(newTime, theme));

        jdbcReservationRepository.update(reservation.modify(newDate, newReservationTime));

        Reservation updated = jdbcReservationRepository.findById(reservation.getId())
                .orElseThrow();

        // then
        assertThat(updated.getDate()).isEqualTo(newDate);
        assertThat(updated.getTime().getId()).isEqualTo(newReservationTime.getId());
    }

    @Test
    @DisplayName("날짜 테마 예약 시간 ID 조회")
    void findAllByDateAndThemeId_success() {
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

    private Theme createTheme(final String name) {
        return jdbcThemeRepository.save(
                Theme.createNew(name, "추리 테마", "https://example.com/theme.png")
        );
    }

}
