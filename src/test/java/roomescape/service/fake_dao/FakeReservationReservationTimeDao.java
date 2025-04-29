package roomescape.service.fake_dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain_entity.Id;
import roomescape.domain_entity.ReservationTime;

public class FakeReservationReservationTimeDao implements ReservationTimeDao {

    private final List<ReservationTime> fakeMemory = new ArrayList<>();
    private long id = 1L;

    @Override
    public List<ReservationTime> findAll() {
        return Collections.unmodifiableList(fakeMemory);
    }

    @Override
    public ReservationTime findById(long id) {
        return fakeMemory.stream()
                .filter(reservationTime -> reservationTime.getId() == id)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public long create(ReservationTime time) {
        long timeId = id++;
        fakeMemory.add(time.copyWithId(new Id(timeId)));
        return timeId;
    }

    @Override
    public void deleteById(Id id) {
        fakeMemory.removeIf(reservationTime -> reservationTime.getId() == id.value());
    }
}
