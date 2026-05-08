package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.JdbcThemeRepository;

@RoomescapeRepositoryTest
class JdbcReservationTimeRepositoryTest {

    private JdbcReservationTimeRepository jdbcReservationTimeRepository;
    private JdbcThemeRepository jdbcThemeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcReservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        jdbcThemeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("예약 시간 전체 조회")
    void reservationTime_findAll_test() {
        //given & when
        LocalTime time = LocalTime.parse("11:00");
        Theme theme = createTheme("미술관의 밤");

        ReservationTime nonIdReservationTime = ReservationTime.createNew(time, theme);
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
        Theme theme = createTheme("미술관의 밤");
        ReservationTime nonIdReservationTime = ReservationTime.createNew(time, theme);

        //when
        ReservationTime result = jdbcReservationTimeRepository.save(nonIdReservationTime);
        ReservationTime saved = jdbcReservationTimeRepository.findById(result.getId())
                .orElseThrow();
        //then
        assertThat(result).isEqualTo(saved);
    }

    @Test
    @DisplayName("테마 예약 시간 전체 조회")
    void reservationTime_theme_time_finaAll_test() {
        //given
        Theme theme = createTheme("미술관의 밤");
        ReservationTime nonIdReservationTime1 = ReservationTime.createNew(LocalTime.parse("11:00"), theme);
        ReservationTime nonIdReservationTime2 = ReservationTime.createNew(LocalTime.parse("13:00"), theme);

        jdbcReservationTimeRepository.save(nonIdReservationTime1);
        jdbcReservationTimeRepository.save(nonIdReservationTime2);

        //when
        List<ReservationTime> reservationTimes = jdbcReservationTimeRepository.findAllByThemeId(theme.getId());

        //then
        assertThat(reservationTimes.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("예약 시간 저장 예외")
    void save_duplicate_test() {
        //given
        LocalTime time = LocalTime.parse("11:00");
        Theme theme = createTheme("미술관의 밤");

        //when
        jdbcReservationTimeRepository.save(ReservationTime.createNew(time, theme));

        //then
        assertThrows(DataIntegrityViolationException.class, () -> {
            jdbcReservationTimeRepository.save(ReservationTime.createNew(time, theme));
        });
    }

    @Test
    @DisplayName("예약 시간 삭제")
    void reservationTime_delete_test() {
        // given
        LocalTime time = LocalTime.parse("11:00");
        Theme theme = createTheme("미술관의 밤");
        ReservationTime nonIdReservationTime = ReservationTime.createNew(time, theme);
        ReservationTime reservationTime = jdbcReservationTimeRepository.save(nonIdReservationTime);
        int beforeSize = jdbcReservationTimeRepository.findAll().size();

        // when
        jdbcReservationTimeRepository.deleteById(reservationTime.getId());

        // then
        int afterSize = jdbcReservationTimeRepository.findAll().size();

        assertThat(afterSize).isEqualTo(beforeSize - 1);
    }

    private Theme createTheme(final String name) {
        return jdbcThemeRepository.save(
                Theme.createNew(name, "추리 테마", "https://example.com/theme.png")
        );
    }

}
