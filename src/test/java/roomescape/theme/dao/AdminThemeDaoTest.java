package roomescape.theme.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import roomescape.theme.domain.Theme;

@JdbcTest
@Import(ThemeDao.class)
public class AdminThemeDaoTest {
    private static final RowMapper<Theme> rowMapper =
            (rs, rowNum) -> {
                return new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image"));
            };

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ThemeDao themeDao;

    @Test
    void 테마_전체_조회_테스트() {
        List<Theme> themes = themeDao.selectAll();
        assertThat(themes.size()).isEqualTo(11);
    }

    @Test
    void 테마_단일_조회_테스트() {
        Theme firstTheme = themeDao.selectById(1L);
        assertThat(firstTheme.getName()).isEqualTo("은하수");

        Theme secoundTheme = themeDao.selectById(2L);
        assertThat(secoundTheme.getName()).isEqualTo("지구");
    }

    @Test
    void 테마_생성_테스트() {
        Theme theme = new Theme("디스커버리", "디스커버리 테마방입니다", "http.jp");
        Theme expected = themeDao.insert(theme);

        String sql = "SELECT * FROM theme WHERE id = ?";
        Theme actual = jdbcTemplate.query(sql, rowMapper, expected.getId()).getFirst();

        assertThat(expected.getId()).isEqualTo(actual.getId());
        assertThat(expected.getName()).isEqualTo(theme.getName());
        assertThat(expected.getDescription()).isEqualTo(theme.getDescription());
        assertThat(expected.getImage()).isEqualTo(theme.getImage());
    }

    @Test
    void 테마_삭제_테스트() {
        long id = 1L;
        themeDao.deleteById(id);

        List<Theme> themes = themeDao.selectAll();
        assertThat(themes.size()).isEqualTo(10);
    }
}
