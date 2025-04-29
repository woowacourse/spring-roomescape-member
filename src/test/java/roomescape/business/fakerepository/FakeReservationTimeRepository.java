package roomescape.business.fakerepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import roomescape.business.ReservationTime;
import roomescape.persistence.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> reservationTimes = new ArrayList<>();

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimes;
    }

    @Override
    public ReservationTime findById(Long id) {
        return reservationTimes.stream()
                .filter(time -> Objects.equals(time.getId(), id))
                .findFirst()
                .get();
    }

    @Override
    public Long add(ReservationTime reservationTime) {
        if (reservationTime.getId() == null) {
            reservationTime.setId(1L);
        }
        reservationTimes.add(reservationTime);
        return reservationTime.getId();
    }

    @Override
    public void delete(Long id) {
        ReservationTime reservationTime = findById(id);
        reservationTimes.remove(reservationTime);
    }
}
