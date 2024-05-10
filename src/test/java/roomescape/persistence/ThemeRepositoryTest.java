package roomescape.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservation.ReservationTimeRepository;
import roomescape.domain.theme.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class ThemeRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ThemeRepository themeRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        themeRepository = new H2ThemeRepository(jdbcTemplate, jdbcTemplate.getDataSource());
        reservationTimeRepository = new H2ReservationTimeRepository(jdbcTemplate, jdbcTemplate.getDataSource());
        reservationRepository = new H2ReservationRepository(jdbcTemplate, jdbcTemplate.getDataSource());
    }

    @DisplayName("모든 테마를 조회한다")
    @Test
    void when_findAllThemes_then_returnAllThemes() {
        // given
        List<Theme> themes = List.of(Fixture.theme1, Fixture.theme2, Fixture.theme3);
        themes.forEach(theme -> themeRepository.save(theme));

        // when, then
        assertThat(themeRepository.findAll())
                .hasSize(3);
    }

    @DisplayName("테마를 id로 조회한다")
    @Test
    void when_findThemeById_then_return() {
        // given
        Theme savedTheme = themeRepository.save(Fixture.theme);

        // when, then
        assertThat(themeRepository.findById(savedTheme.getId()))
                .isPresent();
    }

    @DisplayName("테마를 저장한다")
    @Test
    void when_saveTheme_then_saved() {
        // when
        Theme savedTheme = themeRepository.save(Fixture.theme);

        // then
        assertThat(savedTheme.getId())
                .isNotNull();
    }

    @DisplayName("썸네일이 존재하지 않은 테마를 저장한다")
    @Test
    void when_saveThemeWithoutThumbnail_then_saved() {
        // when
        Theme savedTheme = themeRepository.save(new Theme("테마", "테마 설명"));

        // then
        assertThat(savedTheme.getId())
                .isNotNull();
    }

    @DisplayName("존재하지 않는 테마를 삭제할 경우, 예외가 발생하지 않는다")
    @Test
    void when_deleteThemeDoesNotExist_then_doesNotThrowsException() {
        // when, then
        assertThatCode(() -> themeRepository.deleteById(1000L))
                .doesNotThrowAnyException();
    }

    @DisplayName("테마를 삭제할 경우, 참조된 예약이 있으면 예외를 발생한다")
    @Test
    void when_deleteReferencedTheme_then_throwsException() {
        // given
        ReservationTime savedReservationTime = reservationTimeRepository.save(Fixture.reservationTime);
        Theme savedTheme = themeRepository.save(Fixture.theme);
        reservationRepository.save(new Reservation("피케이", LocalDate.now(), savedReservationTime, savedTheme));

        // when, then
        assertThatThrownBy(() -> themeRepository.deleteById(savedTheme.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 테마를 참조하는 예약이 존재합니다.");
    }

    @DisplayName("테마를 삭제한다")
    @Test
    void when_deleteThemeById_then_deleted() {
        // given
        Theme savedTheme = themeRepository.save(Fixture.theme);

        // when
        themeRepository.deleteById(savedTheme.getId());

        // then
        assertThat(themeRepository.findAll())
                .isEmpty();
    }

    private static class Fixture {
        private static final Theme theme = new Theme("테마", "테마 설명", "https://1.jpg");
        private static final Theme theme1 = new Theme("테마1", "테마1 설명", "https://1.jpg");
        private static final Theme theme2 = new Theme("테마1", "테마1 설명", "https://1.jpg");
        private static final Theme theme3 = new Theme("테마1", "테마1 설명", "https://1.jpg");
        private static final ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
    }
}
