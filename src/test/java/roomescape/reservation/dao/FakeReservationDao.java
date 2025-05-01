package roomescape.reservation.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import roomescape.reservation.Reservation;
import roomescape.reservationtime.ReservationTime;

public class FakeReservationDao implements ReservationDao {

    private final List<Reservation> fakeReservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    public FakeReservationDao(Reservation... reservations) {
        Arrays.stream(reservations).forEach(reservation -> fakeReservations.add(reservation));
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(fakeReservations);
    }

    @Override
    public Long create(Reservation reservation) {
        Reservation reservationWithId = Reservation.of(index.getAndIncrement(), reservation.getName(),
                reservation.getDate(), reservation.getReservationTime(), reservation.getTheme());
        fakeReservations.add(reservationWithId);
        return reservationWithId.getId();
    }

    @Override
    public void delete(Long id) {
        fakeReservations.removeIf(reservation -> reservation.getId().equals(id));
    }

    @Override
    public Optional<Reservation> findByTimeId(Long id) {
        return fakeReservations.stream()
                .filter(reservation -> reservation.getReservationTime().getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return fakeReservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Reservation> findByDateTime(LocalDate date, LocalTime time) {
        return fakeReservations.stream()
                .filter(reservation -> reservation.getDate().equals(date) && reservation.getReservationTime()
                        .getStartAt().equals(time))
                .findFirst();
    }

    @Override
    public List<ReservationTime> findAvailableTimesByDateAndThemeId(LocalDate date, Long themeId) {
        List<ReservationTime> allTimes = List.of(
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new ReservationTime(2L, LocalTime.of(11, 0))
        );

        Set<Long> reservedTimeIds = fakeReservations.stream()
                .filter(reservation -> reservation.getDate().equals(date))
                .filter(reservation -> reservation.getTheme().getId().equals(themeId))
                .map(reservation -> reservation.getReservationTime().getId())
                .collect(Collectors.toSet());

        return allTimes.stream()
                .filter(time -> !reservedTimeIds.contains(time.getId()))
                .sorted(Comparator.comparing(reservationTime -> reservationTime.getStartAt()))
                .collect(Collectors.toList());

    }
}
