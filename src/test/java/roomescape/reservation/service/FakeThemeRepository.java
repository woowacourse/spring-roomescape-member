package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository {

    private final List<Theme> themes;
    private final ReservationRepository reservationRepository;
    private AtomicLong index = new AtomicLong(0);

    public FakeThemeRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
        this.themes = new ArrayList<>();
    }

    @Override
    public Long save(Theme theme) {
        long currentIndex = index.incrementAndGet();
        themes.add(Theme.createWithId(currentIndex, theme.getName(), theme.getDescription(), theme.getThumbnail()));
        return currentIndex;
    }

    @Override
    public List<Theme> findPopularThemes(LocalDate start, LocalDate end) {
        List<Reservation> reservations = reservationRepository.findAll();
        List<Reservation> filterReservations = reservations.stream()
                .filter(reservation -> !(reservation.getDate().isBefore(start) || reservation.getDate().isAfter(end)))
                .toList();

        Map<Theme, Long> themes = new HashMap<>();
        for (Reservation filterReservation : filterReservations) {
            themes.put(filterReservation.getTheme(), themes.getOrDefault(filterReservation.getTheme(), 0L) + 1);
        }

        return themes.entrySet().stream()
                .sorted(Map.Entry.<Theme, Long>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public Theme findById(Long id) {
        return themes.stream()
                .filter(theme -> Objects.equals(theme.getId(), id))
                .findAny()
                .orElseThrow();
    }

    @Override
    public List<Theme> findAll() {
        return Collections.unmodifiableList(themes);
    }

    @Override
    public int deleteById(Long id) {
        Optional<Theme> findTheme = themes.stream()
                .filter(theme -> Objects.equals(theme.getId(), id))
                .findAny();

        if (findTheme.isEmpty()) {
            return 0;
        }

        Theme theme = findTheme.get();
        themes.remove(theme);
        return 1;
    }
}
