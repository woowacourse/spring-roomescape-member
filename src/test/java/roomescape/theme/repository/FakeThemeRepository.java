package roomescape.theme.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.model.Theme;

public class FakeThemeRepository implements ThemeRepository {
    private final List<Theme> themes = new ArrayList<>();

    private final ReservationRepository reservationRepository;

    public FakeThemeRepository(final ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Theme save(final Theme theme) {
        Theme newTheme = Theme.of((long) themes.size() + 1, theme);
        themes.add(newTheme);
        return newTheme;
    }

    @Override
    public List<Theme> findAll() {
        return Collections.unmodifiableList(themes);
    }

    @Override
    public Optional<Theme> findById(final Long id) {
        int index = id.intValue() - 1;
        if (themes.size() > index) {
            return Optional.of(themes.get(index));
        }
        return Optional.empty();
    }

    @Override
    public List<Theme> findOrderByReservation(int size) {
        Map<Theme, List<Reservation>> reservationsByTheme = reservationRepository.findAll().stream()
                .collect(Collectors.groupingBy(Reservation::getTheme));

        return reservationsByTheme.entrySet().stream()
                .sorted(Comparator.comparing(entry -> -entry.getValue().size()))
                .flatMap(themeListEntry -> themeListEntry.getValue().stream().map(Reservation::getTheme))
                .limit(size)
                .toList();
    }

    @Override
    public boolean existsById(final Long id) {
        return themes.stream()
                .anyMatch(theme -> theme.isSameTo(id));
    }

    @Override
    public void deleteById(final Long id) {
        themes.remove(id.intValue() - 1);
    }
}
