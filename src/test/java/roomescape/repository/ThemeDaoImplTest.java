package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeDaoImplTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ThemeDao themeDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into theme (name, description, thumbnail) values('dodo', 'dodododo', 'url')");
    }

    @AfterEach
    void setDown() {
        jdbcTemplate.update("delete from theme");
    }

    @DisplayName("테마 전체를 조회할 수 있습니다")
    @Test
    void should_find_all() {
        assertThat(themeDao.findAll()).hasSize(1);
    }
}
