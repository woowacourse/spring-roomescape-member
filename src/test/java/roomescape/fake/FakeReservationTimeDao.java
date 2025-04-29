package roomescape.fake;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import roomescape.repository.ReservationTimeDao;
import roomescape.service.reservation.ReservationTime;

public class FakeReservationTimeDao implements ReservationTimeDao {

    List<ReservationTime> times = new ArrayList<>();
    Long index = 1L;

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        ReservationTime newReservationTime = new ReservationTime(index++, reservationTime.getStartAt());
        times.add(newReservationTime);
        return newReservationTime;
    }

    @Override
    public List<ReservationTime> findAll() {
        return times;
    }

    @Override
    public ReservationTime findById(final long id) {
        return times.stream()
                .filter(time -> time.getId() == id)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public boolean isExistsByTime(final LocalTime reservationTime) {
        return times.stream()
                .anyMatch(time -> time.getStartAt().equals(reservationTime));
    }

    @Override
    public boolean isNotExistsById(final long id) {
        return times.stream()
                .noneMatch(time -> time.getId() == id);
    }

    @Override
    public void deleteById(final long id) {
        ReservationTime reservationTime = findById(id);
        times.remove(reservationTime);
    }
}
