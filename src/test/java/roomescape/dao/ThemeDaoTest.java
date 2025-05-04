package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Theme;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.constant.TestConstants.DEFAULT_THEME;

@JdbcTest
public class ThemeDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    ThemeDao themeDao;

    @BeforeEach
    void setUp() {
        themeDao = new ThemeDao(jdbcTemplate);
    }

    @Test
    void 테마를_저장할_수_있다() {
        // given
        String themeName = "우아한테크코스";
        Theme theme = new Theme(
                themeName,
                "재밌다",
                "abc.jpg"
        );

        // when
        Theme savedTheme = themeDao.save(theme);

        // then
        assertThat(savedTheme.getName()).isEqualTo(themeName);
    }

    @Test
    void 특정_테마를_조회할_수_있다() {
        // given
        Long id = 1L;
        themeDao.save(DEFAULT_THEME);
        Theme theme = themeDao.findById(id);

        // when & then
        assertThat(theme.getId()).isEqualTo(id);
    }

    @Test
    void 전체_테마를_조회할_수_있다() {
        // given
        int totalSize = themeDao.findAll().size();
        String themeName = "진격의거인";
        Theme theme = new Theme(
                themeName,
                "재밌다",
                "abc.jpg"
        );

        // when
        themeDao.save(theme);

        // when & then
        assertThat(themeDao.findAll().size()).isEqualTo(totalSize + 1);
    }

    @Test
    void 특정_테마를_삭제할_수_있다() {
        // given
        int totalSize = themeDao.findAll().size();

        // when
        themeDao.deleteById(1L);

        // then
        assertThat(themeDao.findAll().size()).isEqualTo(totalSize - 1);
    }
}
