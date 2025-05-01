package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {
    private final List<Reservation> reservations;

    private AtomicLong index = new AtomicLong(0);

    public FakeReservationRepository() {
        reservations = new ArrayList<>();
    }

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public boolean existByReservationTimeId(Long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> Objects.equals(reservation.getTime().getId(), timeId));
    }

    @Override
    public boolean existBy(Long themeId, LocalDate date, LocalTime time) {
        return reservations.stream()
                .anyMatch(reservation ->
                        reservation.getTime().getStartAt().equals(time)
                                && reservation.getDate().equals(date)
                                && reservation.getTheme().getId().equals(themeId));
    }

    @Override
    public boolean existBy(Long themeId) {
        return reservations.stream()
                .anyMatch(reservation -> Objects.equals(reservation.getTheme().getId(), themeId));
    }

    @Override
    public Long save(Reservation reservation) {
        long currentIndex = index.incrementAndGet();

        reservations.add(reservation.assignId(currentIndex));
        return currentIndex;
    }

    @Override
    public int deleteById(Long id) {
        Optional<Reservation> findReservation = reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getId(), id))
                .findAny();

        if (findReservation.isEmpty()) {
            return 0;
        }

        Reservation reservation = findReservation.get();
        reservations.remove(reservation);
        return 1;
    }

    @Override
    public List<Reservation> findBy(LocalDate date, Long themeId) {
        return reservations.stream()
                .filter(reservation -> reservation.getDate().equals(date) && reservation.getTheme().getId().equals(themeId))
                .toList();
    }
}
