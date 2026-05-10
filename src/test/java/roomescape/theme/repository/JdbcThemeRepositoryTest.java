package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.exception.ThemeNotFoundException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class JdbcThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void 테마를_저장하는_테스트() {
        String name = "테마";
        String description = "테마 설명";
        String thumbnailUrl = "https://example.com/theme.png";

        Theme theme = Theme.create(name, description, thumbnailUrl, Theme.RUNTIME);
        Theme savedTheme = themeRepository.save(theme);

        assertThat(savedTheme.getId()).isPositive();
        assertThat(savedTheme.getName()).isEqualTo(name);
        assertThat(savedTheme.getDescription()).isEqualTo(description);
        assertThat(savedTheme.getThumbnailUrl()).isEqualTo(thumbnailUrl);
        assertThat(savedTheme.getRuntime()).isEqualTo(Theme.RUNTIME);
    }

    @Test
    void 테마를_조회하는_테스트() {
        String name = "테마";
        String description = "테마 설명";
        String thumbnailUrl = "https://example.com/theme.png";

        Theme theme = Theme.create(name, description, thumbnailUrl, Theme.RUNTIME);
        Theme savedTheme = themeRepository.save(theme);

        Theme foundTheme = themeRepository.findById(savedTheme.getId())
                .orElseThrow(() -> new ThemeNotFoundException(savedTheme.getId()));

        assertThat(foundTheme.getId()).isEqualTo(savedTheme.getId());
        assertThat(foundTheme.getName()).isEqualTo(name);
        assertThat(foundTheme.getDescription()).isEqualTo(description);
        assertThat(foundTheme.getThumbnailUrl()).isEqualTo(thumbnailUrl);
        assertThat(foundTheme.getRuntime()).isEqualTo(Theme.RUNTIME);
    }

    @Test
    void 모든_테마를_조회하는_테스트() {
        Theme theme1 = Theme.create("테마1", "테마 설명1", "https://example.com/theme1.png", Theme.RUNTIME);
        Theme theme2 = Theme.create("테마2", "테마 설명2", "https://example.com/theme2.png", Theme.RUNTIME);

        Theme savedTheme1 = themeRepository.save(theme1);
        Theme savedTheme2 = themeRepository.save(theme2);

        List<Theme> themes = themeRepository.findAll();

        assertThat(themes).contains(savedTheme1, savedTheme2);
    }

    @Test
    void 테마를_삭제하는_테스트() {
        Theme theme = Theme.create("테마", "테마 설명", "https://example.com/theme.png", Theme.RUNTIME);
        Theme savedTheme = themeRepository.save(theme);

        themeRepository.deleteById(savedTheme.getId());

        List<Theme> themes = themeRepository.findAll();
        assertThat(themes)
                .extracting(Theme::getId)
                .doesNotContain(savedTheme.getId());
    }

    @Test
    void 테마를_삭제하면_이를_참조하는_예약도_삭제된다() {
        ReservationTime reservationTime = reservationTimeRepository.save(LocalTime.of(11, 0));

        Theme theme = Theme.create("테마", "테마 설명", "https://example.com/theme.png", Theme.RUNTIME);
        Theme savedTheme = themeRepository.save(theme);

        Reservation reservation = Reservation.create(
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
    void 인기있는_테마를_조회하는_테스트() {
        List<Theme> themes = themeRepository.findPopularThemes(7, 10);

        assertThat(themes).hasSize(5);
        assertThat(themes)
                .extracting(Theme::getName)
                .containsExactly("테마5", "테마4", "테마3", "테마2", "테마1");
    }

}
