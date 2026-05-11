package roomescape.repository.theme;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;

@Import({JdbcThemeRepository.class})
@JdbcTest
class JdbcThemeRepositoryTest {

    private static final Theme THEME = new Theme(
        null,
        new ThemeName("name"),
        "description",
        ThemeImageUrl.defaultImageUrl());

    private final ThemeRepository themeRepository;

    @Autowired
    public JdbcThemeRepositoryTest(ThemeRepository themeRepository) {
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
        assertThat(themeRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void 저장된_모든_테마를_조회한다() {
        // given
        Theme saved1 = themeRepository.createTheme(
            new Theme(null, new ThemeName("테마1"), "-", ThemeImageUrl.defaultImageUrl()));
        Theme saved2 = themeRepository.createTheme(
            new Theme(null, new ThemeName("테마2"), "-", ThemeImageUrl.defaultImageUrl()));

        // when
        List<Theme> all = themeRepository.findAll();

        // then
        assertThat(all).containsExactlyInAnyOrder(saved1, saved2);
    }

    @Test
    @Sql("/theme-popular-test-data.sql")
    void 최근_1주일간_예약이_많은_테마_상위_10개를_조회할_수_있다() {
        // given
        List<Theme> expected = themeRepository.findAll()
            .stream()
            .sorted(Comparator.comparingLong(Theme::getId).reversed())
            .toList();

        // when
        List<Theme> themes = themeRepository.findWeekPopularThemesOrderByRank(10);

        // then
        assertThat(themes).containsExactlyElementsOf(expected);
    }
}
