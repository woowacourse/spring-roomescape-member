package roomescape.service.fake_dao;

import java.time.LocalTime;
import java.util.List;
import roomescape.dao.TimeDao;
import roomescape.domain_entity.Id;
import roomescape.domain_entity.ReservationTime;

public class OneDataStub implements TimeDao {

    private long id = 1L;

    @Override
    public List<ReservationTime> findAll() {
        return List.of(new ReservationTime(new Id(1L), LocalTime.of(10, 0, 0)));
    }

    @Override
    public ReservationTime findById(long id) {
        return new ReservationTime(new Id(1L), LocalTime.of(10, 0, 0));

    }

    @Override
    public long create(ReservationTime time) {
        return 1L;
    }

    @Override
    public void deleteById(Id id) {
    }
}
