package roomescape.fake;

import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class FakeReservationDao {

    private static final AtomicLong IDX = new AtomicLong();
    private static final Map<Long, Reservation> RESERVATIONS = new HashMap<>();

    public Reservation save(Reservation reservation) {
        Long id = IDX.getAndIncrement();
        Reservation newReservation = new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
        RESERVATIONS.put(id, newReservation);
        return newReservation;
    }

    public List<Reservation> findAll() {
        return RESERVATIONS.values().stream().toList();
    }

    public int deleteById(Long id) {
        if (RESERVATIONS.containsKey(id)) {
            RESERVATIONS.remove(id);
            return 1;
        }
        return 0;
    }

    public boolean existByTimeId(Long timeId) {
        long count = RESERVATIONS.entrySet().stream()
                .filter(e -> e.getValue().getTime().getId().equals(timeId))
                .count();
        return count != 0;
    }

    public boolean existByThemeId(Long themeId) {
        long count = RESERVATIONS.entrySet().stream()
                .filter(e -> e.getValue().getTheme().getId().equals(themeId))
                .count();
        return count != 0;
    }

    public boolean existByTimeIdAndThemeIdAndDate(Long timeId, Long themeId, LocalDate date) {
        long count = RESERVATIONS.entrySet().stream()
                .filter(e -> e.getValue().getTime().getId().equals(timeId)
                        && e.getValue().getTheme().getId().equals(themeId)
                        && e.getValue().getDate().equals(date))
                .count();
        return count != 0;
    }

    public List<Long> findBookedTimes(final Long themeId, final LocalDate date) {
        return RESERVATIONS.values().stream()
                .filter(reservation -> reservation.getTheme().getId().equals(themeId) && reservation.getDate().equals(date))
                .map(reservation -> reservation.getTime().getId())
                .toList();
    }

    public void clear() {
        RESERVATIONS.clear();
        IDX.set(0);
    }
}
