package roomescape.support.fake;

import java.util.List;
import java.util.Optional;
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationdate.ReservationDateRepository;

public class FakeReservationDateRepository implements ReservationDateRepository {

    public ReservationDate reservationDate;
    public ReservationDate savedReservationDate;
    public List<ReservationDate> findAllResult = List.of();
    public Long deletedId;

    @Override
    public Optional<ReservationDate> findById(Long id) {
        return Optional.ofNullable(reservationDate);
    }

    @Override
    public List<ReservationDate> findAll() {
        return findAllResult;
    }

    @Override
    public ReservationDate save(ReservationDate reservationDate) {
        savedReservationDate = reservationDate;
        return ReservationDate.of(1L, reservationDate.getDate());
    }

    @Override
    public int deleteById(Long id) {
        deletedId = id;
        return 1;
    }
}
