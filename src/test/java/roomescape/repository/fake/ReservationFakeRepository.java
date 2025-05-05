package roomescape.repository.fake;


import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import roomescape.model.Reservation;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;

public class ReservationFakeRepository implements ReservationRepository {

    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1L);

    @Override
    public Optional<Reservation> findById(final Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public Long save(final Reservation reservation) {
        var id = index.getAndIncrement();
        var created = new Reservation(
            id,
            reservation.name(),
            reservation.date(),
            reservation.timeSlot(),
            reservation.theme()
        );
        reservations.put(id, created);
        return id;
    }

    @Override
    public Boolean removeById(final Long id) {
        Reservation removed = reservations.remove(id);
        return removed != null;
    }

    @Override
    public List<Reservation> findAll() {
        return List.copyOf(reservations.values());
    }

    @Override
    public List<Reservation> findAllByTimeSlotId(final Long id) {
        return reservations.values().stream()
            .filter(reservation -> Objects.equals(reservation.timeSlot().id(), id))
            .toList();
    }

    @Override
    public List<Reservation> findAllByThemeId(final Long id) {
        return reservations.values().stream()
            .filter(reservation -> Objects.equals(reservation.theme().id(), id))
            .toList();
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(final LocalDate date, final Long themeId) {
        return reservations.values().stream()
            .filter(r -> r.date().equals(date) && Objects.equals(r.theme().id(), themeId))
            .toList();
    }

    @Override
    public List<Theme> findPopularThemesByPeriod(final LocalDate startDate, final LocalDate endDate,
                                                 final Integer limit) {
        var themeCounts = reservations.values().stream()
            .filter(r -> isBetween(r.date(), startDate, endDate))
            .collect(Collectors.groupingBy(Reservation::theme, Collectors.counting()));

        return themeCounts.keySet()
            .stream()
            .sorted(Comparator.comparingLong(themeCounts::get))
            .limit(limit)
            .toList();
    }

    private boolean isBetween(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return (date.isAfter(startDate) || date.isEqual(startDate)) && (date.isEqual(endDate) || date.isBefore(endDate));
    }
}
