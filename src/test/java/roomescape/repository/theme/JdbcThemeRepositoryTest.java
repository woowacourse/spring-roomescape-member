package roomescape.repository.theme;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationLocalDate;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;
import roomescape.repository.reservation.JdbcReservationRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.time.JdbcReservationTimeRepository;
import roomescape.repository.time.ReservationTimeRepository;

@Import({JdbcReservationRepository.class, JdbcReservationTimeRepository.class, JdbcThemeRepository.class})
@JdbcTest
class JdbcThemeRepositoryTest {

    private static final Theme THEME = new Theme(
        null,
        new ThemeName("name"),
        "description",
        ThemeImageUrl.defaultImageUrl());

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
    void 테마를_저장한다() {
        // when
        Theme saved = themeRepository.createTheme(THEME);

        // then
        assertThat(saved.getNameValue()).isEqualTo(THEME.getNameValue());
        assertThat(saved.getDescription()).isEqualTo(THEME.getDescription());
        assertThat(saved.getImageUrlValue()).isEqualTo(THEME.getImageUrlValue());
    }

    @Test
    void 테마를_아이디로_삭제한다() {
        // given
        Theme saved = themeRepository.createTheme(THEME);

        // when
        themeRepository.deleteById(saved.getId());

        // then
        assertThat(themeRepository.findAll()).isEmpty();
    }

    @Test
    void 저장된_모든_테마를_조회한다() {
        // given
        Theme saved1 = themeRepository.createTheme(new Theme(null, new ThemeName("테마1"), "-", ThemeImageUrl.defaultImageUrl()));
        Theme saved2 = themeRepository.createTheme(new Theme(null, new ThemeName("테마2"), "-", ThemeImageUrl.defaultImageUrl()));

        // when
        List<Theme> all = themeRepository.findAll();

        // then
        assertThat(all).containsExactlyInAnyOrder(saved1, saved2);
    }

    @Test
    void 최근_1주일간_예약이_많은_테마_상위_10개를_조회할_수_있다() {
        // given
        List<ReservationTime> times = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            times.add(timeRepository.createReservationTime(new ReservationTime(LocalTime.of(i, 0))));
        }
        List<Theme> tenPopularThemesOrderByRank = createAndSaveTenThemes();

        for (int i = 0; i < tenPopularThemesOrderByRank.size(); i++) {
            for (int j = 0; j < i + 1; j++) {
                reservationRepository.createReservation(new Reservation(
                    null,
                    new MemberName("name"),
                    new ReservationLocalDate(LocalDate.now().minusDays(1)),
                    times.get(j),
                    tenPopularThemesOrderByRank.get(i)
                ));
            }
        }
        Collections.reverse(tenPopularThemesOrderByRank);

        // when
        List<Theme> themes = themeRepository.findWeekPopularThemesOrderByRank(10);

        // then
        assertThat(themes).containsExactlyElementsOf(tenPopularThemesOrderByRank);
    }

    private List<Theme> createAndSaveTenThemes() {
        List<Theme> themes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            themes.add(new Theme(null, new ThemeName("테마" + i), "테마" + i, ThemeImageUrl.defaultImageUrl()));
        }
        return themes.stream()
            .map(themeRepository::createTheme)
            .toList();
    }
}