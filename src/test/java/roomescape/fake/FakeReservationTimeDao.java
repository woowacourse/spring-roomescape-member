package roomescape.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import roomescape.domain.ReservationTime;
import roomescape.persistence.ReservationTimeDao;

public class FakeReservationTimeDao implements ReservationTimeDao {

    private List<ReservationTime> reservationTimes = new ArrayList<>();
    private Long id = 0L;

    public FakeReservationTimeDao(ReservationTime... reservationTimes) {
        this((long) reservationTimes.length, new ArrayList<>(List.of(reservationTimes)));
    }

    private FakeReservationTimeDao(Long id, List<ReservationTime> reservationTimes) {
        this.id = id;
        this.reservationTimes = reservationTimes;
    }

    @Override
    public List<ReservationTime> findAll() {
        return List.copyOf(reservationTimes);
    }

    @Override
    public Long create(ReservationTime reservationTime) {
        reservationTimes.add(new ReservationTime(++id, reservationTime.startAt()));
        return id;
    }

    @Override
    public void deleteById(Long reservationTimeId) {
        reservationTimes = reservationTimes.stream()
                .filter(reservationTime -> reservationTime.id() != reservationTimeId)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReservationTime> findById(Long reservationTimeId) {
        return reservationTimes.stream()
                .filter(reservationTime -> reservationTime.id() == reservationTimeId)
                .findFirst();
    }
}
