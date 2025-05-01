package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.model.Theme;

class ThemeDaoTest {

    private static ThemeDao dao;
    private static JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new ThemeDao(jdbcTemplate);
    }

    @Test
    void 저장_성공() {
        // given
        Theme theme = new Theme(null, "test", "testDescript", "testThumb");

        // when
        Theme saved = dao.save(theme);

        // then
        List<Theme> themes = findByJdbc();
        assertThat(themes).hasSize(17);
        assertThat(themes.getLast().getId()).isEqualTo(saved.getId());
    }

    @Test
    void 삭제_성공() {
        // given
        Long id = 16L;

        // when
        boolean deleted = dao.deleteById(id);

        // then
        assertThat(deleted).isTrue();
        List<Theme> themes = findByJdbc();
        assertThat(themes).hasSize(15);
    }

    @Test
    void 삭제할_아이디_없는_경우() {
        // given
        Long id = 999L;

        // when
        boolean deleted = dao.deleteById(id);

        // then
        assertThat(deleted).isFalse();
        List<Theme> themes = findByJdbc();
        assertThat(themes).hasSize(16);
    }

    @Test
    void 모든_테마_반환() {
        // when
        List<Theme> all = dao.findAll();

        // then
        assertThat(all).hasSize(16);
    }

    @Test
    void id로_테마_조회() {
        // given
        Long id = 1L;

        // when
        Optional<Theme> found = dao.findById(id);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(1L);
    }

    @Test
    void 없는_id로_테마_조회() {
        // given
        Long id = 999L;

        // when
        Optional<Theme> found = dao.findById(id);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void 예약_건_수가_많은_순서대로_10개의_테마_조회() {
        // when
        List<Theme> topTenTheme = dao.getTopTenTheme();

        // then
        List<Long> topTenIds = topTenTheme.stream().map(Theme::getId).toList();
        assertThat(topTenTheme).hasSize(10);
        assertThat(topTenIds).contains(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
    }

    @Test
    void 이미_존재하는_테마의_여부_확인() {
        // given
        String existName = "첫번째";
        String nonExistName = "test";

        // when
        boolean exist = dao.isExistThemeName(existName);
        boolean nonExist = dao.isExistThemeName(nonExistName);

        // then
        assertThat(exist).isTrue();
        assertThat(nonExist).isFalse();
    }

    private List<Theme> findByJdbc() {
        return jdbcTemplate.query("""
                        SELECT id, name, description, thumbnail
                        FROM THEME
                        """,
                (rs, rowNum) -> new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                )
        );
    }
}
