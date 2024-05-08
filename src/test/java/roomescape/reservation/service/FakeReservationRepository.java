package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.reservation.repository.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    Map<Long, Reservation> reservations;
    AtomicLong reservationAtomicLong = new AtomicLong(0);
    AtomicLong reservationTimeAtomicLong = new AtomicLong(0);
    AtomicLong themeAtomicLong = new AtomicLong(0);

    public FakeReservationRepository() {
        this.reservations = new HashMap<>();
    }

    FakeReservationRepository(List<Reservation> reservations) {
        this.reservations = new HashMap<>();
        for (int i = 0; i < reservations.size(); i++) {
            this.reservations.put(reservations.get(i).getId(), reservations.get(i));
        }
    }

    @Override
    public List<Reservation> findAll() {
        return reservations.values().stream().toList();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        if (reservations.containsKey(id)) {
            return Optional.of(reservations.get(id));
        }
        return Optional.empty();
    }

    @Override
    public Reservation insert(Reservation reservation) {
        Long id = reservationAtomicLong.incrementAndGet();

        ReservationTime reservationTime = new ReservationTime(reservationTimeAtomicLong.incrementAndGet(),
                reservation.getTime().getStartAt());

        Theme theme = reservation.getTheme();
        Theme addTheme = new Theme(themeAtomicLong.incrementAndGet(), theme.getName(), theme.getDescription(),
                theme.getDescription());

        Reservation addReservation = new Reservation(id, reservation.getName(), reservation.getDate(),
                reservationTime, addTheme);

        reservations.put(id, addReservation);
        return reservation;
    }

    @Override
    public boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        return reservations.values().stream()
                .anyMatch(reservation -> reservation.getTimeId().equals(timeId) && reservation.getDate().equals(date)
                        && reservation.getThemeId().equals(themeId));
    }

    @Override
    public void deleteById(Long id) {
        reservations.remove(id);
    }

    @Override
    public List<ReservationTime> findTimesByDateAndTheme(LocalDate date, Long themeId) {
        return reservations.values()
                .stream()
                .filter(reservation ->
                        reservation.getDate().isEqual(date) && reservation.getThemeId().equals(themeId))
                .map(reservation -> {
                    return new ReservationTime(reservation.getTimeId(), reservation.getTime().getStartAt());
                })
                .toList();
    }

    @Override
    public List<Theme> findThemeOrderByReservationCount() {
        return null;
    }
}
