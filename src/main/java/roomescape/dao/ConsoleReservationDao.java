package roomescape.dao;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

public class ConsoleReservationDao implements ReservationDao {

    public static final int INITIAL_ID_VALUE = 1;
    private final AtomicLong reservationId = new AtomicLong(INITIAL_ID_VALUE);
    private final Map<Long, Reservation> store = new ConcurrentHashMap<>();

    @Override
    public List<Reservation> readAll() {
        return store.values().stream().toList();
    }

    @Override
    public List<Long> readTimeIdsByDateAndThemeId(ReservationDate reservationDate, Long themeId) {
        return List.of();
    }

    @Override
    public Reservation create(Reservation reservation) {
        long id = reservationId.getAndIncrement();
        Reservation newReservation = new Reservation(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getReservationTime(),
                reservation.getTheme()
        );
        store.put(id, newReservation);
        return newReservation;
    }

    @Override
    public Boolean exist(long id) {
        return store.containsKey(id);
    }

    @Override
    public Boolean exist(ReservationDate reservationDate, ReservationTime reservationTime, Theme theme) {
        return store.values().stream()
                .anyMatch(value -> value.getDate() == reservationDate
                        && value.getReservationTime() == reservationTime
                        && value.getTheme() == theme);
    }

    @Override
    public void delete(long id) {
        store.remove(id);
    }

    @Override
    public boolean existByTimeId(Long timeId) {
        return store.values().stream()
                .anyMatch(reservation -> Objects.equals(reservation.getReservationTime().getId(), timeId));
    }

    @Override
    public boolean existByThemeId(Long themeId) {
        return store.values().stream()
                .anyMatch(reservation -> Objects.equals(reservation.getTheme().getId(), themeId));
    }
}
