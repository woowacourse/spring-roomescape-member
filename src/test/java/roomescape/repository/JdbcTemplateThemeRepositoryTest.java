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
}
