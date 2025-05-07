package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.jdbc.Sql;
import roomescape.model.Theme;

@JdbcTest
@Import(ThemeDao.class)
@Sql(scripts = {"/schema.sql", "/data.sql"})
class ThemeDaoTest {

    private static ThemeDao dao;
    private static NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        dao = new ThemeDao(namedParameterJdbcTemplate);
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
        List<Theme> topFiveTheme = dao.getPopularThemeByRankAndDuration(
                5,
                LocalDate.of(2023, 3, 11),
                LocalDate.of(2023, 3, 25)
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
        boolean exist = dao.isExistThemeName(existName);
        boolean nonExist = dao.isExistThemeName(nonExistName);

        // then
        assertThat(exist).isTrue();
        assertThat(nonExist).isFalse();
    }

    private List<Theme> findByJdbc() {
        return namedParameterJdbcTemplate.query("""
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
