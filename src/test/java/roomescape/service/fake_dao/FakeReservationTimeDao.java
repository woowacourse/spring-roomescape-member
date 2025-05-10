package roomescape.service.fake_dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import roomescape.dao.ReservationTimeDao;
import roomescape.entity.ReservationTime;

public class FakeReservationTimeDao implements ReservationTimeDao {

    private final List<ReservationTime> fakeMemory = new ArrayList<>();
    private Long id = 1L;

    @Override
    public List<ReservationTime> findAll() {
        return Collections.unmodifiableList(fakeMemory);
    }

    @Override
    public ReservationTime findById(Long id) {
        return fakeMemory.stream()
                .filter(reservationTime -> reservationTime.getId() == id)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public ReservationTime create(ReservationTime time) {
        Long timeId = id++;
        ReservationTime createdReservationTime = time.copyWithId(timeId);
        fakeMemory.add(createdReservationTime);
        return createdReservationTime;
    }

    @Override
    public void deleteById(Long id) {
        fakeMemory.removeIf(reservationTime -> reservationTime.getId() == id);
    }

    @Override
    public boolean existsByStartAt(ReservationTime reservationTime) {
        return false;
    }
}
