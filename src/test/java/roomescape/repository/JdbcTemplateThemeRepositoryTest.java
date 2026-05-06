package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@Sql("/test-theme.sql")
class JdbcTemplateThemeRepositoryTest {
    private static final int DEFAULT_THEME_SIZE = 10;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        this.themeRepository = new JdbcTemplateThemeRepository(jdbcTemplate);
    }

    @Test
    void 모든_테마_조회() {
        List<Theme> themes = themeRepository.findAll();

        assertThat(themes.size()).isEqualTo(DEFAULT_THEME_SIZE);
    }

    @Test
    void 테마를_저장한다() {
        Theme theme = new Theme(null, "마법 학교", "마법 학교의 마지막 시험을 통과하세요.", "https://example.com/theme10.jpg");

        Theme savedTheme = themeRepository.save(theme);

        assertThat(savedTheme.id()).isNotNull();
        assertThat(savedTheme.name()).isEqualTo("마법 학교");
        assertThat(savedTheme.description()).isEqualTo("마법 학교의 마지막 시험을 통과하세요.");
        assertThat(savedTheme.thumbnailUrl()).isEqualTo("https://example.com/theme10.jpg");
    }

    @Test
    void 테마를_삭제한다() {
        Theme theme = new Theme(null, "마법 학교", "마법 학교의 마지막 시험을 통과하세요.", "https://example.com/theme10.jpg");
        long id = themeRepository.save(theme).id();

        themeRepository.delete(id);
        assertThat(themeRepository.findAll().size()).isEqualTo(DEFAULT_THEME_SIZE);
    }
}
