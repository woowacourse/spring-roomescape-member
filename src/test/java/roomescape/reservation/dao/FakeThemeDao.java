package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.dto.ReservationMember;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ThemeRepository;

public class FakeThemeDao implements ThemeRepository {
    private final Map<Long, Theme> themes = new HashMap<>();
    private final ReservationRepository reservationRepository;

    public FakeThemeDao(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Theme> findAll() {
        return themes.values().stream()
                .toList();
    }

    @Override
    public Theme save(final Theme theme) {
        themes.put((long) themes.size() + 1, theme);
        return new Theme((long) themes.size(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public boolean deleteById(final long themeId) {
        if (!themes.containsKey(themeId) && containsThemeId(themeId)) {
            return false;
        }
        themes.remove(themeId);
        return true;
    }

    private boolean containsThemeId(long themeId) {
        return reservationRepository.findAll().stream()
                .anyMatch(reservationMember -> reservationMember.reservation().getTheme().getId() == themeId);
    }

    @Override
    public Optional<Theme> findById(final long themeId) {
        return Optional.of(themes.get(themeId));
    }

    @Override
    public List<Theme> findPopularThemes() {
        LocalDate startDate = LocalDate.now().minusDays(7);

        List<ReservationMember> reservationMembers = reservationRepository.findAll();

        Map<Long, Long> themeReservationCounts = reservationMembers.stream()
                .filter(reservationMember -> reservationMember.reservation().getDate().isAfter(startDate))
                .collect(Collectors.groupingBy(
                        reservationMember -> reservationMember.reservation().getTheme().getId(),
                        Collectors.counting()
                ));

        List<Long> popularThemeIds = themeReservationCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();

        return popularThemeIds.stream()
                .map(themes::get)
                .collect(Collectors.toList());
    }
}
