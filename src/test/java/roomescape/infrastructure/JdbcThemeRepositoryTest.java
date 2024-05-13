package roomescape.infrastructure;

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
import roomescape.domain.theme.ThemeRepository;
import roomescape.fixture.ThemeFixture;

@JdbcTest
@Sql("/theme.sql")
class JdbcThemeRepositoryTest {
    private final ThemeRepository themeRepository;

    @Autowired
    JdbcThemeRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.themeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    void 테마를_저장한다() {
        Theme theme = ThemeFixture.theme("포레스트");

        Theme savedTheme = themeRepository.save(theme);

        assertAll(
                () -> assertThat(savedTheme.getName()).isEqualTo(theme.getName()),
                () -> assertThat(savedTheme.getDescription()).isEqualTo(theme.getDescription()),
                () -> assertThat(savedTheme.getThumbnail()).isEqualTo(theme.getThumbnail())
        );
    }

    @Test
    void 모든_테마를_조회한다() {
        List<Theme> themes = themeRepository.findAll();

        assertThat(themes).hasSize(3);
    }

    @Test
    void 테마를_삭제한다() {
        boolean isDeleted = themeRepository.deleteById(2L);

        assertThat(isDeleted).isTrue();
    }

    @Test
    void 존재하지_않는_테마를_삭제하면_0을_반환한다() {
        boolean isDeleted = themeRepository.deleteById(0L);

        assertThat(isDeleted).isFalse();
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
