package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import roomescape.BasicAcceptanceTest;
import roomescape.TestFixtures;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;

@Sql("/setTheme.sql")
class H2ThemeRepositoryTest extends BasicAcceptanceTest {
    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("DB에 저장된 전체 테마를 반환한다")
    @Test
    void findAll() {
        List<Theme> expectedThemes = List.of(TestFixtures.THEME_1, TestFixtures.THEME_2);

        assertThat(themeRepository.findAll()).isEqualTo(expectedThemes);
    }

    @DisplayName("해당 id의 테마를 반환한다")
    @Test
    void findById() {
        assertThat(themeRepository.findById(1L).orElseThrow()).isEqualTo(TestFixtures.THEME_1);
    }

    @DisplayName("테마를 DB에 저장한다")
    @Test
    void save() {
        themeRepository.save(TestFixtures.THEME_3);

        List<Theme> expectedThemes = List.of(TestFixtures.THEME_1, TestFixtures.THEME_2, TestFixtures.THEME_3);

        assertThat(themeRepository.findAll()).isEqualTo(expectedThemes);
    }

    @DisplayName("해당 id의 테마를 삭제한다")
    @Test
    void deleteById() {
        themeRepository.deleteById(1L);

        List<Theme> expectedThemes = List.of(TestFixtures.THEME_2);

        assertThat(themeRepository.findAll()).isEqualTo(expectedThemes);
    }
}
