package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.entity.Theme;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@ActiveProfiles("test")
@Sql(scripts = {"/schema.sql", "/test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class H2ThemeDaoTest {

    private ThemeDao themeDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        themeDao = new H2ThemeDao(jdbcTemplate);
    }

    @Test
    void 테마를_저장한다() {
        Theme theme = new Theme("인터스텔라", "인터스텔라 방탈출", "link");

        Theme saved = themeDao.save(theme);

        Theme expected = new Theme(4L, "인터스텔라", "인터스텔라 방탈출", "link");
        assertThat(saved).isEqualTo(expected);
    }

    @Test
    void 모든_테마를_조회한다() {
        List<Theme> allThemes = themeDao.findAll();

        assertThat(allThemes).hasSize(3);
    }

    @Test
    void 테마를_삭제한다() {
        themeDao.save(new Theme("인셉션", "인셉션 방탈출", "link"));

        themeDao.deleteById(4L);

        List<Theme> allThemes = themeDao.findAll();
        assertThat(allThemes).hasSize(3);
    }

    @Test
    void 최근_일주일간_인기있는_테마를_10개까지_조회한다() {
        List<Theme> themes = themeDao.sortByRank();

        List<Object> expected = List.of(
            new Theme(2L, "", "", ""),
            new Theme(1L, "", "", ""),
            new Theme(3L, "", "", "")
        );
        assertThat(themes).isEqualTo(expected);
    }

    @Test
    void 동일한_이름의_테마를_확인한다() {
        assertAll(
            () -> assertThat(themeDao.isExistByName("인터스텔라")).isTrue(),
            () -> assertThat(themeDao.isExistByName("고질라")).isFalse()
        );
    }
}