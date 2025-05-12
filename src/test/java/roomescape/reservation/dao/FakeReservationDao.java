package roomescape.reservation.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import roomescape.reservation.Reservation;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

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
    public List<Reservation> findAll(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        return new ArrayList<>(fakeReservations);
    }

    @Override
    public Long create(Reservation reservation) {
        Reservation reservationWithId = Reservation.of(index.getAndIncrement(), reservation.getMember(),
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

        List<ReservationTime> nonAvailableReservations = fakeReservations.stream()
                .filter(reservation -> reservation.getDate().equals(date))
                .filter(reservation -> reservation.getTheme().getId().equals(themeId))
                .map(reservation -> reservation.getReservationTime())
                .toList();

        return allTimes.stream()
                .filter(t -> !nonAvailableReservations.contains(t))
                .toList();
    }

    @Override
    public List<Theme> findTop10Themes(LocalDate currentDate) {
        LocalDate startDate = currentDate.minusDays(8);
        Map<Theme, Long> themeCounts = fakeReservations.stream()
                .filter(reservation -> reservation.getDate().isBefore(currentDate))
                .filter(reservation -> reservation.getDate().isAfter(startDate))
                .collect(Collectors.groupingBy(reservation -> reservation.getTheme(), Collectors.counting()));

        return themeCounts.keySet().stream().sorted(new Comparator<Theme>() {
                    @Override
                    public int compare(Theme o1, Theme o2) {
                        return (int) (themeCounts.get(o1) - themeCounts.get(o2));
                    }
                })
                .limit(10)
                .toList();
    }
}
