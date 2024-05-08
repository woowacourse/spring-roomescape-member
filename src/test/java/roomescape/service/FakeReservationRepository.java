package roomescape.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;
import roomescape.repository.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {
    private final Map<Long, Reservation> fakeReservationDB = new HashMap<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public List<Reservation> findAll() {
        return fakeReservationDB.values().stream().toList();
    }

    @Override
    public Reservation save(Reservation reservation) {
        long idx = index.getAndIncrement();
        Reservation savedReservation = new Reservation(idx, reservation.getName(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
        fakeReservationDB.put(idx, savedReservation);
        return savedReservation;
    }

    @Override
    public void deleteById(Long id) {
        fakeReservationDB.remove(id);
    }

    @Override
    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        return fakeReservationDB.values().stream()
                .filter(reservation -> reservation.getThemeId().equals(themeId))
                .filter(reservation -> reservation.getDate().equals(date))
                .toList();
    }

    @Override
    public List<Reservation> findByPeriod(LocalDate startDate, LocalDate endDate) {
        return fakeReservationDB.values().stream()
                .filter(reservation -> reservation.getDate().isAfter(startDate) && reservation.getDate().isBefore(endDate))
                .toList();
    }

    @Override
    public List<Reservation> findSearchReservation(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        return null;
    }

    public void deleteAll() {
        fakeReservationDB.clear();
    }
}
