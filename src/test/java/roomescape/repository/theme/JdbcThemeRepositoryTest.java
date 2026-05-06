package roomescape.repository.theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.util.List;
import roomescape.repository.reservation.JdbcReservationRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.JdbcReservationTimeRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@Import({JdbcReservationRepository.class, JdbcReservationTimeRepository.class, JdbcThemeRepository.class})
@JdbcTest
class JdbcThemeRepositoryTest {

    private static final Theme THEME = new Theme(null, "name", "description", "image-url");

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;

    @Autowired
    public JdbcThemeRepositoryTest(
        ReservationRepository repository,
        ReservationTimeRepository timeRepository,
        ThemeRepository themeRepository
    ) {
        this.reservationRepository = repository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    @Test
    void 테마_추가_테스트() {
        // given & when
        Theme saved = themeRepository.createTheme(THEME);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo(THEME.getName());
        assertThat(saved.getDescription()).isEqualTo(THEME.getDescription());
        assertThat(saved.getImageUrl()).isEqualTo(THEME.getImageUrl());
    }

    @Test
    void 기존에_저장된_테마를_id로_찾아서_삭제한다() {
        // given
        Theme saved = themeRepository.createTheme(THEME);

        // when & then
        assertThatCode(() -> themeRepository.deleteById(saved.getId()))
            .doesNotThrowAnyException();
    }

    @Test
    void 저장된_모든_테마를_조회한다() {
        // given
        String first = "테마1";
        String second = "테마2";
        themeRepository.createTheme(new Theme(first, "-", "url"));
        themeRepository.createTheme(new Theme(second, "-", "url"));

        // when
        List<Theme> all = themeRepository.findAll();

        // then
        assertThat(all).hasSize(2);
        assertThat(all).extracting(Theme::getName)
                .anySatisfy(name -> assertThat(name).isEqualTo(first))
                .anySatisfy(name -> assertThat(name).isEqualTo(second));
    }

    @Test
    void 최근_1주일간_예약이_많은_테마_상위_10개를_조회할_수_있다() {
        // given
        List<ReservationTime> times = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            times.add(timeRepository.createReservationTime(
                new ReservationTime(LocalTime.of(i, 0))));
        }
        List<Theme> tenPopularThemesOrderByRank = createAndSaveTenThemes();

        for (int i = 0; i < tenPopularThemesOrderByRank.size(); i++) {
            for (int j = 0; j < i + 1; j++) {
                reservationRepository.createReservation(new Reservation(
                    "name", LocalDate.now().minusDays(1), times.get(j), tenPopularThemesOrderByRank.get(i)
                ));
            }
        }
        Collections.reverse(tenPopularThemesOrderByRank);

        // when
        List<Theme> themes = themeRepository.findTop10WeekPopularThemesOrderByRank();

        // then
        assertThat(themes).containsExactlyElementsOf(tenPopularThemesOrderByRank);
    }

    private List<Theme> createAndSaveTenThemes() {
        List<Theme> themes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            themes.add(new Theme("테마" + i, "테마" + i, "테마" + i));
        }

        return themes.stream()
            .map(themeRepository::createTheme)
            .collect(Collectors.toList());
    }
}