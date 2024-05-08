package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.service.dto.request.ThemeRequest;
import roomescape.service.dto.response.ThemeResponse;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.theme.ThemeRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ThemeService {
    public static final int START_DATE_DIFF = 8;
    public static final int END_DATE_DIFF = 1;
    public static final int TOP_LIMIT_COUNT = 10;

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse save(ThemeRequest themeRequest) {
        Theme theme = themeRequest.toDomain();

        Theme savedTheme = themeRepository.save(theme);

        return ThemeResponse.from(savedTheme);
    }

    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeRepository.findAll();

        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> findTopThemes() {
        List<Reservation> pastReservations = findPastReservations(START_DATE_DIFF, END_DATE_DIFF);

        return findMostReservedThemes(pastReservations, TOP_LIMIT_COUNT).stream()
                .map(ThemeResponse::from)
                .toList();
    }

    private List<Reservation> findPastReservations(long startDateDiff, long endDateDiff) {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusDays(startDateDiff);
        LocalDate endDate = currentDate.minusDays(endDateDiff);
        return reservationRepository.findByPeriod(startDate, endDate);
    }

    private List<Theme> findMostReservedThemes(List<Reservation> reservations, int limitCount) {
        Map<Theme, Long> reservationsGroupedByTheme = reservations.stream()
                .collect(Collectors.groupingBy(Reservation::getTheme, Collectors.counting()));

        return reservationsGroupedByTheme.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limitCount)
                .map(Map.Entry::getKey)
                .toList();
    }

    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }
}
