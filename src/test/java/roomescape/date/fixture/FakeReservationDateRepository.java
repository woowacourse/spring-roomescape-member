package roomescape.date.fixture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import roomescape.date.domain.ReservationDate;
import roomescape.date.repository.ReservationDateRepository;

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

    public List<ReservationDate> saveAll(List<ReservationDate> reservationDates) {
        List<ReservationDate> savedReservationDates = new ArrayList<>();
        for (ReservationDate reservationDate : reservationDates) {
            ReservationDate savedReservationDate = save(reservationDate);
            savedReservationDates.add(savedReservationDate);
        }
        return savedReservationDates;
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
