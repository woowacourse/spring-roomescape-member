package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Theme;

@JdbcTest
class JdbcThemeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcThemeDao jdbcThemeDao;

    @BeforeEach
    void setUp() {
        jdbcThemeDao = new JdbcThemeDao(jdbcTemplate);
    }

    @Test
    @DisplayName("테마 목록을 조회한다")
    void returnThemeList() {
        // when
        List<Theme> themes = jdbcThemeDao.findAll();
        Theme theme = themes.getFirst();

        // then
        assertAll(
                () -> assertThat(themes).hasSize(1),
                () -> assertThat(theme)
                        .extracting("id", "name", "description", "thumbnail")
                        .containsExactly(1L, "name", "description", "thumbnail")
        );
    }

    @Test
    @DisplayName("테마 목록중 하나를 조회한다")
    void returnThemeById() {
        // when
        Theme theme = jdbcThemeDao.findById(1L);

        // then
        assertThat(theme)
                .extracting("id", "name", "description", "thumbnail")
                .containsExactly(1L, "name", "description", "thumbnail");
    }

    @Test
    @DisplayName("테마를 저장한다")
    void saveTheme() {
        // given
        Theme theme = new Theme("name", "description", "thumbnail");

        // when
        Long savedThemeId = jdbcThemeDao.save(theme);
        Boolean result = existsThemeById(savedThemeId);

        // then
        assertThat(savedThemeId).isNotNull();
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("테마를 삭제한다")
    void deleteTheme() {
        // given
        Boolean exists = existsThemeById(1L);

        // when
        jdbcThemeDao.deleteById(1L);
        Boolean notExists = existsThemeById(1L);

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    private Boolean existsThemeById(Long savedThemeId) {
        return jdbcTemplate.queryForObject(
                "select exists(select 1 from theme where id = ?)",
                Boolean.class,
                savedThemeId
        );
    }
}
