package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.config.TestFixture.reservation;
import static roomescape.config.TestFixture.reservationTime;
import static roomescape.config.TestFixture.theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.exception.ThemeNotFoundException;

@JdbcTest
@Import({JdbcThemeRepository.class, JdbcReservationTimeRepository.class, JdbcReservationRepository.class})
class JdbcThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void 테마를_저장하는_테스트() {
        Theme theme = theme("테마");

        Theme savedTheme = themeRepository.save(theme);

        assertThat(savedTheme.getId()).isPositive();
        assertThat(savedTheme.getName()).isEqualTo(theme.getName());
        assertThat(savedTheme.getDescription()).isEqualTo(theme.getDescription());
        assertThat(savedTheme.getThumbnailUrl()).isEqualTo(theme.getThumbnailUrl());
        assertThat(savedTheme.getRuntime()).isEqualTo(Theme.RUNTIME);
    }

    @Test
    void 테마를_조회하는_테스트() {
        Theme theme = theme("테마");
        Theme savedTheme = themeRepository.save(theme);

        Theme foundTheme = themeRepository.findById(savedTheme.getId())
                .orElseThrow(() -> new ThemeNotFoundException(savedTheme.getId()));

        assertThat(foundTheme.getId()).isEqualTo(savedTheme.getId());
        assertThat(foundTheme.getName()).isEqualTo(theme.getName());
        assertThat(foundTheme.getDescription()).isEqualTo(theme.getDescription());
        assertThat(foundTheme.getThumbnailUrl()).isEqualTo(theme.getThumbnailUrl());
        assertThat(foundTheme.getRuntime()).isEqualTo(Theme.RUNTIME);
    }

    @Test
    void 모든_테마를_조회하는_테스트() {
        Theme theme1 = theme("테마1");
        Theme theme2 = theme("테마2");

        Theme savedTheme1 = themeRepository.save(theme1);
        Theme savedTheme2 = themeRepository.save(theme2);

        List<Theme> themes = themeRepository.findAll();

        assertThat(themes).contains(savedTheme1, savedTheme2);
    }

    @Test
    void 테마를_삭제하는_테스트() {
        Theme theme = theme("테마");
        Theme savedTheme = themeRepository.save(theme);

        themeRepository.deleteById(savedTheme.getId());

        List<Theme> themes = themeRepository.findAll();
        assertThat(themes)
                .extracting(Theme::getId)
                .doesNotContain(savedTheme.getId());
    }

    @Test
    void 테마를_삭제하면_이를_참조하는_예약도_삭제된다() {
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTime(LocalTime.of(11, 0)));

        Theme theme = theme("테마");
        Theme savedTheme = themeRepository.save(theme);

        Reservation reservation = reservation(
                "밀란",
                LocalDate.of(2026, 5, 6),
                reservationTime,
                savedTheme
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        themeRepository.deleteById(savedTheme.getId());

        Optional<Reservation> foundReservation = reservationRepository.findById(savedReservation.getId());
        assertThat(foundReservation).isEmpty();
    }

    @Sql("/create_dummies_for_popular_themes.sql")
    @Test
    void 최근_1주_동안_인기있는_테마를_조회하는_테스트() {
        LocalDate today = LocalDate.of(2026, 5, 8);
        LocalDate start = today.minusDays(7);

        List<Theme> themes = themeRepository.findPopularThemes(start, today, 10);

        assertThat(themes).hasSize(5);
        assertThat(themes)
                .extracting(Theme::getName)
                .containsExactly("테마5", "테마4", "테마3", "테마2", "테마1");
    }

}
