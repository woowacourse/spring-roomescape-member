package roomescape.dao.time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.TimeDao;
import roomescape.dao.TimeJdbcDao;

@JdbcTest
@Import(TimeJdbcDao.class)
@ActiveProfiles("test")
class TimeJdbcDaoTest extends TimeDaoContract {

    @Autowired
    private TimeDao timeDao;
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    protected TimeDao dao() {
        return timeDao;
    }

    @Override
    protected void clear() {
        jdbc.execute("DELETE FROM times");
    }
}
