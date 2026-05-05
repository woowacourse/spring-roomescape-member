package roomescape.date.fixture;

import roomescape.date.domain.ReservationDate;
import roomescape.date.repository.ReservationDateRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeReservationDateRepository implements ReservationDateRepository {
    private final List<ReservationDate> reservationDates = new ArrayList<>();
    private Long autoIncrement = 0L;

    @Override
    public Optional<ReservationDate> findById(Long id) {
        return reservationDates.stream()
                .filter(reservationDate -> reservationDate.id() == id)
                .findFirst();
    }

    @Override
    public List<ReservationDate> findAll() {
        return reservationDates.stream().toList();
    }

    @Override
    public ReservationDate save(ReservationDate reservationDate) {
        autoIncrement();
        ReservationDate savedReservationDate = ReservationDate.load(autoIncrement, reservationDate.date());
        this.reservationDates.add(savedReservationDate);
        return savedReservationDate;
    }

    private void autoIncrement() {
        autoIncrement = autoIncrement + 1;
    }

    @Override
    public boolean delete(Long id) {
        Optional<ReservationDate> findDate = findById(id);
        if (findDate.isEmpty()) {
            return false;
        }

        return reservationDates.remove(findDate.get());
    }
}
