package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import roomescape.member.dto.CompletedReservation;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.Theme;
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
        long id = (long) themes.size() + 1;
        themes.put(id, new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail()));
        return new Theme((long) themes.size(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public boolean deleteById(final long themeId) {
        if (!themes.containsKey(themeId)) {
            return false;
        }
        themes.remove(themeId);
        return true;
    }

    @Override
    public Theme findById(final long themeId) {
        return themes.get(themeId);
    }

    @Override
    public List<Theme> findPopularThemes(int datePeriod, int themeCount) {
        LocalDate startDate = LocalDate.now().minusDays(datePeriod);

        List<CompletedReservation> reservations = reservationRepository.findAll();

        Map<Long, Long> themeReservationCounts = reservations.stream()
                .filter(reservation -> reservation.date().isAfter(startDate))
                .collect(Collectors.groupingBy(
                        reservation -> reservation.theme().getId(),
                        Collectors.counting()
                ));

        List<Long> popularThemeIds = themeReservationCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(themeCount)
                .map(Map.Entry::getKey)
                .toList();

        return popularThemeIds.stream()
                .map(themes::get)
                .collect(Collectors.toList());
    }
}
