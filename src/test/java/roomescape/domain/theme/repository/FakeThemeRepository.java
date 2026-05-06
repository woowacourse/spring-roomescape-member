package roomescape.domain.theme.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.entity.Theme;

public class FakeThemeRepository implements ThemeRepository {

    private final AtomicLong id = new AtomicLong(0);
    private final List<Theme> themes = new ArrayList<>();

    private final AtomicLong reservationId = new AtomicLong(0);
    private final List<Reservation> reservations = new ArrayList<>();

    public void saveAllReservations(List<Reservation> reservations) {
        List<Reservation> reconstructedReservations = reservations.stream()
            .map(reservation -> Reservation.reconstruct(
                reservationId.addAndGet(1),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()))
            .toList();
        this.reservations.addAll(reconstructedReservations);
    }

    public void saveAllThemes(List<Theme> themes) {
        List<Theme> reconstructedThemes = themes.stream()
            .map(theme -> Theme.reconstruct(
                id.addAndGet(1),
                theme.getName(),
                theme.getDescription(),
                theme.getImageUrl()))
            .toList();
        this.themes.addAll(reconstructedThemes);
    }

    @Override
    public List<Theme> findAllThemes() {
        return Collections.unmodifiableList(themes);
    }

    @Override
    public Theme save(Theme theme) {
        Theme savedTheme = Theme.reconstruct(id.addAndGet(1), theme.getName(), theme.getDescription(),
            theme.getImageUrl());
        themes.add(savedTheme);
        return savedTheme;
    }

    @Override
    public void deleteThemeById(Long id) {
        themes.removeIf(time -> Objects.equals(time.getId(), id));
    }

    @Override
    public Optional<Theme> findThemeById(Long id) {
        return themes.stream()
            .filter(time -> Objects.equals(time.getId(), id))
            .findFirst();
    }

    @Override
    public List<Theme> findPopularThemesDateBetween(LocalDate startDate, LocalDate endDate, Integer limit) {
        List<Theme> filteredThemes = reservations.stream()
            .filter(reservation -> reservation.getDate().isAfter(startDate) || reservation.getDate().isEqual(startDate))
            .filter(reservation -> reservation.getDate().isBefore(endDate) || reservation.getDate().isEqual(endDate))
            .map(Reservation::getTheme)
            .toList();
        List<Theme> distinctThemes = filteredThemes.stream()
            .distinct()
            .toList();
        Map<Long, Long> counterMap = getThemeCounterMap(filteredThemes, distinctThemes);

        return distinctThemes.stream()
            .sorted(Comparator.comparingLong((Theme theme) -> counterMap.get(theme.getId()))
                .reversed())
            .limit(limit)
            .toList();
    }

    private Map<Long, Long> getThemeCounterMap(List<Theme> themes, List<Theme> distinctThemes) {
        Map<Long, Long> counterMap = new LinkedHashMap<>();
        for (Theme theme : distinctThemes) {
            long count = themes.stream()
                .filter(filteredTheme -> filteredTheme.equals(theme))
                .count();
            counterMap.put(theme.getId(), count);
        }

        return counterMap;
    }
}
