package roomescape.dao.theme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.ThemeDao;
import roomescape.dao.ThemeJdbcDao;

@JdbcTest
@Import({
        ThemeJdbcDao.class
})
@ActiveProfiles("test")
class ThemeJdbcDaoTest extends ThemeDaoContract {

    @Autowired
    private ThemeDao themeDao;
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    ThemeDao dao() {
        return themeDao;
    }

    @Override
    void clear() {
        jdbc.execute("DELETE FROM themes");
    }
}
