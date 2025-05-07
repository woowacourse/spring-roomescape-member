package roomescape.theme.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.theme.Theme;

import org.junit.jupiter.api.Test;

@Import(JdbcThemeDao.class)
@JdbcTest
@Sql({"/sql/test-schema.sql", "/sql/test-data.sql"})
public class JdbcThemeDaoTest {
    @Autowired
    private JdbcThemeDao jdbcThemeDao;

    @Test
    void 테마를_추가할_수_있다() {
        // given
        Theme theme = Theme.createWithoutId("레벨2", "탈출하자",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        // when
        Long themeId = jdbcThemeDao.create(theme);

        // then
        assertThat(themeId).isEqualTo(6L);
    }

    @Test
    void 전체_테마를_조회한다() {
        // when
        List<Theme> allTheme = jdbcThemeDao.findAll();
        // then
        assertThat(allTheme).hasSize(5);
        assertThat(allTheme.getFirst().getName()).isEqualTo("테마1");
        assertThat(allTheme.get(1).getName()).isEqualTo("테마2");
    }

    @Test
    void 테마를_삭제한다() {
        // when
        jdbcThemeDao.delete(5L);
        // then
        List<Theme> allTheme = jdbcThemeDao.findAll();
        assertThat(allTheme).hasSize(4);
    }
}
