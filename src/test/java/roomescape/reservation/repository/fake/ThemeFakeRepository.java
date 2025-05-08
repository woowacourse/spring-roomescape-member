package roomescape.reservation.repository.fake;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ThemeRepository;

public class ThemeFakeRepository implements ThemeRepository {

    private final Map<Long, Theme> themes = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();
    private final ReservationRepository reservationRepository;

    public ThemeFakeRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Long saveAndReturnId(Theme themeWithoutId) {
        long id = idGenerator.incrementAndGet();
        Theme theme = themeWithoutId.withId(id);
        themes.put(id, theme);

        return id;
    }

    @Override
    public List<Theme> findAll() {
        return themes.values().stream()
                .toList();
    }

    @Override
    public int deleteById(Long id) {
        if (themes.containsKey(id)) {
            themes.remove(id);
            return 1;
        }
        return 0;
    }

    @Override
    public Optional<Theme> findById(Long id) {
        if (themes.containsKey(id)) {
            return Optional.of(themes.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<Long> findTopThemeIdByDateRange(LocalDate start, LocalDate end, int limit) {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .filter(reservation -> isWithinPeriod(reservation.getDate(), start, end))
                .collect(Collectors.groupingBy(reservation -> reservation.getTheme().getId(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Entry.<Long, Long>comparingByValue().reversed())
                .limit(limit)
                .map(Entry::getKey)
                .toList();
    }

    @Override
    public List<Theme> findByIdIn(List<Long> ids) {
        return ids.stream()
                .map(themes::get)
                .toList();
    }

    private boolean isWithinPeriod(LocalDate target, LocalDate start, LocalDate end) {
        return (target.isEqual(start) || target.isAfter(start)) &&
                (target.isEqual(end) || target.isBefore(end));
    }

}
