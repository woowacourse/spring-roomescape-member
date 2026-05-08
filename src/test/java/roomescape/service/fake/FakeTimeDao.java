package roomescape.service.fake;

import java.time.LocalTime;
import roomescape.dao.TimeDao;
import roomescape.domain.Time;

public class FakeTimeDao extends FakeCommonDao<Time> implements TimeDao {

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        return store.values()
                .stream()
                .anyMatch(theme -> theme.getStartAt().equals(startAt));
    }

    @Override
    Time create(Time time, Long id) {
        return new Time(id, time.getStartAt());
    }
}
