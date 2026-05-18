package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.config.TestFixture.futureReservationDate;
import static roomescape.config.TestFixture.reservation;
import static roomescape.config.TestFixture.reservationTime;
import static roomescape.config.TestFixture.theme;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.common.exception.DomainType;
import roomescape.common.exception.InUseException;
import roomescape.common.exception.NotFoundException;
import roomescape.config.FixedClockTestConfig;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.entity.Theme;

@JdbcTest
@Import({
        JdbcThemeRepository.class,
        JdbcReservationTimeRepository.class,
        JdbcReservationRepository.class,
        FixedClockTestConfig.class
})
class JdbcThemeRepositoryTest {

    private static final String DEFAULT_THEME_NAME = "테마";
    private static final String FIRST_THEME_NAME = "테마1";
    private static final String SECOND_THEME_NAME = "테마2";
    private static final String RESERVATION_NAME = "밀란";
    private static final LocalTime RESERVATION_START_AT = LocalTime.of(10, 0);
    private static final Long NOT_FOUND_ID = 999L;
    private static final int POPULAR_LIMIT = 10;

    @Autowired
    private Clock clock;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void 테마를_저장하는_테스트() {
        // given
        Theme theme = theme(DEFAULT_THEME_NAME);

        // when
        Theme savedTheme = themeRepository.save(theme);

        // then
        assertThat(savedTheme.getId()).isPositive();
        assertThat(savedTheme.getName()).isEqualTo(theme.getName());
        assertThat(savedTheme.getDescription()).isEqualTo(theme.getDescription());
        assertThat(savedTheme.getThumbnailUrl()).isEqualTo(theme.getThumbnailUrl());
        assertThat(savedTheme.getRuntime()).isEqualTo(Theme.RUNTIME);
    }

    @Test
    void 테마를_조회하는_테스트() {
        // given
        Theme theme = theme(DEFAULT_THEME_NAME);
        Theme savedTheme = themeRepository.save(theme);

        // when
        Theme foundTheme = themeRepository.findById(savedTheme.getId())
                .orElseThrow(() -> new NotFoundException(DomainType.THEME, savedTheme.getId()));

        // then
        assertThat(foundTheme.getId()).isEqualTo(savedTheme.getId());
        assertThat(foundTheme.getName()).isEqualTo(theme.getName());
        assertThat(foundTheme.getDescription()).isEqualTo(theme.getDescription());
        assertThat(foundTheme.getThumbnailUrl()).isEqualTo(theme.getThumbnailUrl());
        assertThat(foundTheme.getRuntime()).isEqualTo(Theme.RUNTIME);
    }

    @Test
    void 테마_존재여부를_확인하는_테스트() {
        // given
        Theme theme = themeRepository.save(theme(DEFAULT_THEME_NAME));

        // when
        boolean existsTheme = themeRepository.existsById(theme.getId());
        boolean existsNotFoundTheme = themeRepository.existsById(NOT_FOUND_ID);

        // then
        assertThat(existsTheme).isTrue();
        assertThat(existsNotFoundTheme).isFalse();
    }

    @Test
    void 모든_테마를_조회하는_테스트() {
        // given
        Theme theme1 = theme(FIRST_THEME_NAME);
        Theme theme2 = theme(SECOND_THEME_NAME);

        Theme savedTheme1 = themeRepository.save(theme1);
        Theme savedTheme2 = themeRepository.save(theme2);

        // when
        List<Theme> themes = themeRepository.findAll();

        // then
        assertThat(themes).contains(savedTheme1, savedTheme2);
    }

    @Test
    void 테마를_삭제하는_테스트() {
        // given
        Theme theme = theme(DEFAULT_THEME_NAME);
        Theme savedTheme = themeRepository.save(theme);

        // when
        themeRepository.deleteById(savedTheme.getId());

        // then
        List<Theme> themes = themeRepository.findAll();
        assertThat(themes)
                .extracting(Theme::getId)
                .doesNotContain(savedTheme.getId());
    }

    @Test
    void 예약이_참조하는_테마는_삭제할_수_없다() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(RESERVATION_START_AT));
        Theme theme = themeRepository.save(theme(DEFAULT_THEME_NAME));
        Reservation reservation = reservation(RESERVATION_NAME, futureReservationDate(clock), reservationTime, theme);
        reservationRepository.save(reservation);

        // when & then
        assertThatThrownBy(() -> themeRepository.deleteById(theme.getId()))
                .isInstanceOf(InUseException.class);
    }

    @Sql("/create_dummies_for_popular_themes.sql")
    @Test
    void 최근_1주_동안_인기있는_테마를_조회하는_테스트() {
        // given
        LocalDate today = LocalDate.now(clock);
        LocalDate start = today.minusDays(7);

        // when
        List<Theme> themes = themeRepository.findPopularThemes(start, today, POPULAR_LIMIT);

        // then
        assertThat(themes).hasSize(5);
        assertThat(themes)
                .extracting(Theme::getName)
                .containsExactly("테마5", "테마4", "테마3", "테마2", "테마1");
    }

}
