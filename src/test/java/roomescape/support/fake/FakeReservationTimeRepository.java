package roomescape.support.fake;

import java.util.List;
import java.util.Optional;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    public ReservationTime reservationTime;
    public ReservationTime savedReservationTime;
    public List<ReservationTime> findAllResult = List.of();
    public Long deletedId;

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return Optional.ofNullable(reservationTime);
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        savedReservationTime = reservationTime;
        return ReservationTime.of(1L, reservationTime.getStartAt());
    }

    @Override
    public List<ReservationTime> findAll() {
        return findAllResult;
    }

    @Override
    public int deleteById(Long id) {
        deletedId = id;
        return 1;
    }
}
