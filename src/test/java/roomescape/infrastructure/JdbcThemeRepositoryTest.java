package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;

@JdbcTest
@Import(JdbcThemeRepository.class)
class JdbcThemeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcThemeRepository jdbcThemeRepository;

    @DisplayName("테마를 저장한다.")
    @Test
    void shouldSaveTheme() {
        Theme theme = new Theme(new ThemeName("테마"), "테마 설명", "url");
        jdbcThemeRepository.save(theme);
        String sql = "select count(*) from theme";
        int count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("모든 테마를 조회한다.")
    @Test
    void shouldFindAllThemes() {
        jdbcTemplate.update("insert into theme (name, description, thumbnail) values (?, ?, ?)",
                "테마", "테마 설명", "url");
        List<Theme> themes = jdbcThemeRepository.findAll();
        assertThat(themes).hasSize(1);
    }

    @DisplayName("id로 테마를 삭제한다.")
    @Test
    void shouldDeleteThemeById() {
        Theme theme = new Theme(new ThemeName("테마"), "테마 설명", "url");
        jdbcTemplate.update("insert into theme (id, name, description, thumbnail) values (?, ?, ?, ?)",
                1L, "테마", "테마 설명", "url");
        jdbcThemeRepository.deleteById(1L);
        List<Theme> themes = jdbcThemeRepository.findAll();
        assertThat(themes).isEmpty();
    }
}
