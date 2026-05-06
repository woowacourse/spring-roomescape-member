package roomescape.theme;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest(properties = "spring.sql.init.mode=never")
@Import(ThemeDao.class)
@Sql(scripts = "classpath:schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:reset-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeDaoTest {
    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 테마를_저장하고_ID로_조회할_수_있다() {
        Theme saved = themeDao.save("Theme A", "desc", "https://example.com/a.png");

        Theme found = themeDao.findById(saved.id()).orElseThrow();

        assertThat(found.id()).isEqualTo(saved.id());
        assertThat(found.name()).isEqualTo("Theme A");
    }

    @Test
    void 테마를_전체_조회할_수_있다() {
        themeDao.save("Theme A", "desc", "https://example.com/a.png");
        themeDao.save("Theme B", "desc", "https://example.com/b.png");

        List<Theme> themes = themeDao.findAll();

        assertThat(themes).hasSize(2);
        assertThat(themes)
                .extracting(Theme::name)
                .containsExactly("Theme A", "Theme B");
    }

    @Test
    void 테마를_삭제할_수_있다() {
        Theme saved = themeDao.save("Theme A", "desc", "https://example.com/a.png");

        themeDao.delete(saved.id());

        assertThat(themeDao.findAll()).isEmpty();
    }

    @Test
    @Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 예약_수_기준으로_테마를_조회할_수_있다() {
        List<Theme> ranked = themeDao.findRanked("reservationCount", "DESC", LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 6), 10L);

        assertThat(ranked).hasSize(4);
        assertThat(ranked.get(0).name()).isEqualTo("Theme A");
        assertThat(ranked.get(1).name()).isEqualTo("Theme B");
        assertThat(ranked.get(2).name()).isEqualTo("Theme C");
        assertThat(ranked.get(3).name()).isEqualTo("Theme D");
    }
}
