package roomescape.theme.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeDao;

class ThemeDaoTest {

    private static ThemeDao themeDao;
    private static JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();

        jdbcTemplate = new JdbcTemplate(dataSource);
        themeDao = new ThemeDao(dataSource);
    }

    @Test
    void 저장_성공() {
        // given
        Theme theme = new Theme(null, "test", "testDescript", "testThumb");

        // when
        Theme saved = ThemeDaoTest.themeDao.save(theme);

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
        boolean deleted = themeDao.deleteById(id);

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
        boolean deleted = themeDao.deleteById(id);

        // then
        assertThat(deleted).isFalse();
        List<Theme> themes = findByJdbc();
        assertThat(themes).hasSize(16);
    }

    @Test
    void id로_테마_조회() {
        // given
        Long id = 1L;

        // when
        Optional<Theme> found = themeDao.findById(id);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(1L);
    }

    @Test
    void 없는_id로_테마_조회() {
        // given
        Long id = 999L;

        // when
        Optional<Theme> found = themeDao.findById(id);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void 예약_건_수가_많은_순서대로_10개의_테마_조회() {
        // when
        List<Theme> topFiveTheme = themeDao.getPopularThemeByRankAndDuration(
                5,
                LocalDate.now().minusDays(15),
                LocalDate.now()
        );

        // then
        List<Long> topTenIds = topFiveTheme.stream().map(Theme::getId).toList();
        assertThat(topFiveTheme).hasSize(5);
        assertThat(topTenIds).contains(6L, 7L, 8L, 9L, 10L);
    }

    @Test
    void 이미_존재하는_테마의_여부_확인() {
        // given
        String existName = "첫번째";
        String nonExistName = "test";

        // when
        boolean exist = themeDao.isExistThemeName(existName);
        boolean nonExist = themeDao.isExistThemeName(nonExistName);

        // then
        assertThat(exist).isTrue();
        assertThat(nonExist).isFalse();
    }

    @Test
    void 존재하는_모든_테마의_개수를_카운팅한다() {
        // when
        int count = themeDao.countTotalTheme();

        // then
        assertThat(count).isEqualTo(16);
    }

    @Test
    void 시작과_끝을_설정해_테마들을_조회() {
        // given
        int start = 3;
        int end = 10;

        // when
        List<Theme> themesWithPage = themeDao.findThemesWithPage(start, end);

        // then
        assertThat(themesWithPage).hasSize(8);
        assertThat(themesWithPage.getFirst().getId()).isEqualTo(3L);
        assertThat(themesWithPage.getLast().getId()).isEqualTo(10L);
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
