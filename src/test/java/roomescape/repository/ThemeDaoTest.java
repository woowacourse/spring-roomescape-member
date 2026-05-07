package roomescape.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Theme;
import roomescape.domain.ThemeStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ThemeDao themeDao;

    private final RowMapper<Theme> rowMapper = (rs, rowNum) -> {

        Theme theme = Theme.of(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("thumbnail_url"),
                rs.getString("description")
        );

        if (rs.getString("status").equals(ThemeStatus.DELETED.toString())) {
            return theme.deleted();
        }

        return theme;
    };

    @Test
    @DisplayName("테마 생성 테스트")
    void ofReservationTimeTest() {
        Theme theme = Theme.pending("새 테마", "test.url", "테스트용 테마");
        Theme saved = themeDao.save(theme);

        Theme themeFromQuery = jdbcTemplate.queryForObject("SELECT * FROM theme WHERE id = ?", rowMapper, saved.id());

        assertThat(themeFromQuery.id()).isEqualTo(saved.id());
        assertThat(themeFromQuery.name()).isEqualTo(saved.name());
        assertThat(themeFromQuery.description()).isEqualTo(saved.description());

        assertThat(themeFromQuery.status()).isEqualTo(ThemeStatus.AVAILABLE);
    }

    @Test
    @DisplayName("테마 삭제 테스트")
    void deleteReservationTimeTest() {
        Theme theme = Theme.pending("새 테마", "test.url", "테스트용 테마");
        Theme saved = themeDao.save(theme);

        themeDao.delete(saved.id());
        Theme themeFromQuery = jdbcTemplate.queryForObject("SELECT * FROM theme WHERE id = ?", rowMapper, saved.id());

        ThemeStatus expected = ThemeStatus.DELETED;
        ThemeStatus actual = themeFromQuery.status();

        assertThat(actual).isEqualTo(expected);
    }
}
