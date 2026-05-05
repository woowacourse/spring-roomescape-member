package roomescape.admin.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ThemeDao themeDao;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("DELETE FROM theme");

        jdbcTemplate.update("INSERT INTO theme(name, description, image) VALUES (?, ?, ?)", "은하수", "은하수 테마방입니다.",
                "http.123.jpg");
        jdbcTemplate.update("INSERT INTO theme(name, description, image) VALUES (?, ?, ?)", "지구", "지구 테마방입니다.",
                "http.123.jpg");
    }

    @Test
    void 데이터베이스_연동() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "THEME", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 테마_전체_조회_테스트() {
        List<Theme> themes = themeDao.findAll();
        assertThat(themes.size()).isEqualTo(2);

        Theme theme = themes.get(0);
        assertThat(theme.getName()).isEqualTo("은하수");
    }

    @Test
    void 테마_단일_조회_테스트() {
        Theme firstTheme = themeDao.findById(1L);
        assertThat(firstTheme.getName()).isEqualTo("은하수");

        Theme secoundTheme = themeDao.findById(2L);
        assertThat(secoundTheme.getName()).isEqualTo("지구");

    }

    @Test
    void 테마_생성_테스트() {
        Theme theme = new Theme("수성", "수성 테마방입니다", "http.jp");
        Theme result = themeDao.insert(theme);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo(theme.getName());
    }
}
