package roomescape.domain.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.theme.Theme;
import roomescape.fixture.ThemeFixture;

@JdbcTest
class JdbcThemeRepositoryTest {
    private final ThemeRepository themeRepository;

    @Autowired
    JdbcThemeRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.themeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    void 테마를_저장한다() {
        Theme theme = ThemeFixture.theme();

        Theme savedTheme = themeRepository.save(theme);

        assertAll(
                () -> assertThat(savedTheme.getName()).isEqualTo(theme.getName()),
                () -> assertThat(savedTheme.getDescription()).isEqualTo(theme.getDescription()),
                () -> assertThat(savedTheme.getThumbnail()).isEqualTo(theme.getThumbnail())
        );
    }

    @Test
    void 모든_테마를_조회한다() {
        Theme theme1 = ThemeFixture.theme("테마1");
        Theme theme2 = ThemeFixture.theme("테마2");
        themeRepository.save(theme1);
        themeRepository.save(theme2);

        List<Theme> themes = themeRepository.findAll();
        assertAll(
                () -> assertThat(themes).hasSize(2),
                () -> assertThat(themes.get(0).getName()).isEqualTo("테마1"),
                () -> assertThat(themes.get(1).getName()).isEqualTo("테마2")
        );
    }

    @Test
    void 테마를_삭제한다() {
        Theme theme = ThemeFixture.theme();
        theme = themeRepository.save(theme);

        themeRepository.deleteById(theme.getId());

        List<Theme> themes = themeRepository.findAll();
        assertThat(themes).isEmpty();
    }

    @Test
    void 같은_테마_이름이_존재하면_true를_반환한다() {
        Theme theme = ThemeFixture.theme();
        theme = themeRepository.save(theme);

        assertThat(themeRepository.existsByName(theme.getName())).isTrue();
    }

    @Test
    void 같은_테마_이름이_존재하지_않으면_false를_반환한다() {
        assertThat(themeRepository.existsByName("오리와 함께 스프링 모험기")).isFalse();
    }

    @Test
    @Sql("/reservation.sql")
    void 일주일_기준_예약이_많은_순서대로_테마를_반환한다() {
        List<Theme> popularThemes = themeRepository.findPopularThemesForWeekLimit10(LocalDate.parse("2024-05-02"));
        assertAll(
                () -> assertThat(popularThemes).hasSize(3),
                () -> assertThat(popularThemes.get(0).getId()).isEqualTo(1),
                () -> assertThat(popularThemes.get(1).getId()).isEqualTo(3),
                () -> assertThat(popularThemes.get(2).getId()).isEqualTo(2)
        );
    }
}
