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
class JdbcThemeRepositoryTest {

    private JdbcThemeRepository jdbcThemeRepository;
    private JdbcReservationTimeRepository jdbcReservationTimeRepository;
    private JdbcReservationRepository jdbcReservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcThemeRepository = new JdbcThemeRepository(jdbcTemplate);
        jdbcReservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        jdbcReservationRepository = new JdbcReservationRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("테마 저장")
    void theme_save_success() {
        // given
        Theme theme = Theme.createNew("미술관의 밤", "추리 테마", "https://example.com/theme.png");

        // when
        Theme result = jdbcThemeRepository.save(theme);
        Theme saved = jdbcThemeRepository.findById(result.getId())
                .orElseThrow();

        // then
        assertThat(saved).isEqualTo(result);
    }

    @Test
    @DisplayName("테마 전체 조회")
    void theme_findAll_success() {
        // given
        createTheme("미술관의 밤");

        // when
        List<Theme> themes = jdbcThemeRepository.findAll();

        // then
        assertThat(themes).hasSize(1);
    }

    @Test
    @DisplayName("테마 이름 중복 저장 예외")
    void theme_save_whenDuplicate_throws() {
        // given
        createTheme("미술관의 밤");

        // when & then
        assertThrows(DataIntegrityViolationException.class, () ->
                jdbcThemeRepository.save(Theme.createNew("미술관의 밤", "새 설명", "https://example.com/new-theme.png"))
        );
    }

    @Test
    @DisplayName("테마 삭제")
    void theme_delete_success() {
        // given
        Theme saved = createTheme("미술관의 밤");
        int beforeSize = jdbcThemeRepository.findAll().size();

        // when
        jdbcThemeRepository.deleteById(saved.getId());

        // then
        int afterSize = jdbcThemeRepository.findAll().size();
        assertThat(afterSize).isEqualTo(beforeSize - 1);
    }

    @Test
    @DisplayName("최근 기간 기준 인기 테마 예약 수 순서대로 조회")
    void findPopularThemes_success() {
        // given
        LocalDate referenceDate = LocalDate.parse("2026-05-06");
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

        jdbcReservationRepository.save(Reservation.createNew("쿠다", referenceDate.minusDays(1), firstThemeTime));
        jdbcReservationRepository.save(Reservation.createNew("아루", referenceDate.minusDays(2), firstThemeTime));
        jdbcReservationRepository.save(Reservation.createNew("도기", referenceDate.minusDays(3), firstThemeTime));

        jdbcReservationRepository.save(Reservation.createNew("포비", referenceDate.minusDays(1), secondThemeTime));
        jdbcReservationRepository.save(Reservation.createNew("솔라", referenceDate.minusDays(2), secondThemeTime));

        jdbcReservationRepository.save(Reservation.createNew("레오", referenceDate.minusDays(1), thirdThemeTime));
        jdbcReservationRepository.save(Reservation.createNew("오래된예약", referenceDate.minusDays(10), thirdThemeTime));

        // when
        List<Theme> popularThemes = jdbcThemeRepository.findPopularThemes(7, 2, referenceDate);

        // then
        assertThat(popularThemes).hasSize(2);
        assertThat(popularThemes.get(0).getId()).isEqualTo(firstTheme.getId());
        assertThat(popularThemes.get(1).getId()).isEqualTo(secondTheme.getId());
    }

    @Test
    @DisplayName("오늘 예약 인기 테마 집계 제외")
    void findPopularThemes_excludeTodayReservation_success() {
        // given
        LocalDate today = LocalDate.parse("2026-11-08");

        Theme firstTheme = createTheme("미술관의 밤");
        Theme secondTheme = createTheme("심해 연구소");

        ReservationTime firstThemeTime = jdbcReservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.parse("10:00"), firstTheme)
        );

        ReservationTime secondThemeTime = jdbcReservationTimeRepository.save(
                ReservationTime.createNew(LocalTime.parse("11:00"), secondTheme)
        );

        // 집계 대상
        jdbcReservationRepository.save(
                Reservation.createNew("쿠다", LocalDate.parse("2026-11-07"), firstThemeTime)
        );
        jdbcReservationRepository.save(
                Reservation.createNew("아루", LocalDate.parse("2026-11-06"), firstThemeTime)
        );

        // 제외 대상
        jdbcReservationRepository.save(
                Reservation.createNew("포비", today, secondThemeTime)
        );
        jdbcReservationRepository.save(
                Reservation.createNew("피케이", LocalDate.parse("2026-10-08"), secondThemeTime)
        );

        // when
        List<Theme> popularThemes =
                jdbcThemeRepository.findPopularThemes(7, 10, today);

        // then
        assertThat(popularThemes).hasSize(1);
        assertThat(popularThemes.get(0).getId()).isEqualTo(firstTheme.getId());
    }

    @Test
    @DisplayName("테마 이름 존재 여부 확인")
    void theme_existsByName_success() {
        // given
        createTheme("미술관의 밤");

        // when
        boolean exists = jdbcThemeRepository.existsByName("미술관의 밤");
        boolean notExists = jdbcThemeRepository.existsByName("놀이공원 탈출");

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    private Theme createTheme(String name) {
        return jdbcThemeRepository.save(
                Theme.createNew(name, "테스트용 설명", "https://example.com/theme.png")
        );
    }
}
