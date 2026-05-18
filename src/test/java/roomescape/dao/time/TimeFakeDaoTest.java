package roomescape.dao.time;

import roomescape.dao.TimeDao;
import roomescape.fixture.FakeDatabase;
import roomescape.fixture.FakeTimeDao;

public class TimeFakeDaoTest extends TimeDaoContract {

    private final FakeTimeDao timeDao = new FakeTimeDao();

    @Override
    TimeDao dao() {
        return timeDao;
    }

    @Override
    void clear() {
        FakeDatabase.clearTime();
    }
}
