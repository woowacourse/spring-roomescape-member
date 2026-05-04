package roomescape.repository.theme;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Theme;

import static org.assertj.core.api.Assertions.assertThat;

@Import(JdbcThemeRepository.class)
@JdbcTest
class JdbcThemeRepositoryTest {
    private final ThemeRepository repository;

    @Autowired
    public JdbcThemeRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.repository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    void 테마_추가_테스트() {
        // given
        Theme theme = new Theme(null, "나폴리탄", "짱 무서움", "image-url");

        // when
        Theme saved = repository.createTheme(theme);

        // then
        assertThat(saved).isNotNull();
    }
}