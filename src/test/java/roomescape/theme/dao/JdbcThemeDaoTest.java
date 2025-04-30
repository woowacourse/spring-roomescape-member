package roomescape.theme.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.theme.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class JdbcThemeDaoTest {
    @Autowired
    private ThemeDao themeDao;

    @Test
    void 테마를_추가할_수_있다() {
        // given

        Theme theme = Theme.createWithoutId("레벨2", "탈출하자",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        // when
        Theme createdTheme = themeDao.create(theme);

        // then
        assertThat(createdTheme.getName()).isEqualTo("레벨2");
    }

    @Test
    void 전체_테마를_조회한다() {
        // given
        Theme theme1 = Theme.createWithoutId("레벨1", "탈출하자",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        Theme theme2 = Theme.createWithoutId("레벨2", "탈출하자",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        themeDao.create(theme1);
        themeDao.create(theme2);
        // when
        List<Theme> allTheme = themeDao.findAll();
        // then
        assertThat(allTheme).hasSize(2);
        assertThat(allTheme.getFirst().getName()).isEqualTo("레벨1");
        assertThat(allTheme.get(1).getName()).isEqualTo("레벨2");
    }

    @Test
    void 테마를_삭제한다() {
        // given
        Theme theme1 = Theme.createWithoutId("레벨1", "탈출하자",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        Theme theme2 = Theme.createWithoutId("레벨2", "탈출하자",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        themeDao.create(theme1);
        themeDao.create(theme2);
        // when
        themeDao.delete(1L);
        // then
        List<Theme> allTheme = themeDao.findAll();
        assertThat(allTheme).hasSize(1);
    }
}
