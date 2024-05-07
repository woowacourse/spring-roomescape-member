package roomescape.theme.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import roomescape.theme.domain.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/schema.sql", "/data.sql"})
class ThemeDaoTest {

    @Autowired
    private ThemeDao themeDao;

    @Test
    void findPopularThemes() {
        List<Theme> themeList = themeDao.findPopularThemes(LocalDate.parse("2024-04-24"), LocalDate.parse("2024-04-30"));
        List<Long> actual = themeList.stream().map(Theme::getId).toList();
        List<Long> expected = List.of(2L, 1L, 3L);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void checkExistThemes() {
        Theme theme = Theme.createWithOutId("정글 모험", "열대 정글의 심연을 탐험하세요.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        assertThat(themeDao.checkExistThemes(theme)).isTrue();
    }
}
