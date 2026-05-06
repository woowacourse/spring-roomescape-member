package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.Theme;
import roomescape.domain.vo.Name;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({
        ThemeJdbcDao.class
})
@ActiveProfiles("test")
public class ThemeJdbcDaoTest {
    private static final int DELETED = 1;

    @Autowired
    private ThemeDao themeDao;

    private Theme theme1;
    private Theme theme2;

    @BeforeEach
    void setUp() {
        theme1 = new Theme(new Name("방 이름"), "url", "설명");
        theme2 = new Theme(new Name("두번째 방이름"), "url2", "설명2");
    }

    @Test
    void findAll() {
        List<Theme> saved = insertThemesHandler(theme1, theme2);
        List<Theme> find = themeDao.findAll();

        assertThat(find).hasSize(saved.size())
                .containsAll(saved);
    }

    @Test
    void findById() {
        Theme saved = insertThemeHandler(theme1);

        assertThat(themeDao.findById(saved.getId()))
                .isPresent()
                .get().isEqualTo(saved);
    }

    @Test
    void insert() {
        Theme saved = insertThemeHandler(theme1);
        assertThat(saved).isNotNull();
    }

    @Test
    void delete() {
        Theme saved = insertThemeHandler(theme1);
        assertThat(themeDao.delete(saved.getId())).isEqualTo(DELETED);
    }


    private List<Theme> insertThemesHandler(Theme... themes) {
        return Arrays.stream(themes)
                .map(this::insertThemeHandler)
                .toList();
    }

    private Theme insertThemeHandler(Theme theme) {
        return themeDao.insert(theme);
    }
}
