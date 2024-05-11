package roomescape.service.theme;

import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.exception.DuplicatedException;
import roomescape.exception.ReferencedReservationExistException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.theme.ThemeRepository;
import roomescape.service.theme.dto.ThemeCreateRequest;
import roomescape.service.theme.dto.ThemeResponse;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ThemeService {

    public static final long POPULAR_THEME_PERIOD = 7L;
    public static final long POPULAR_THEME_COUNT = 10L;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse createTheme(ThemeCreateRequest request) {
        Theme theme = request.toTheme();
        validateDuplicated(theme);
        Theme savedTheme = themeRepository.save(theme);
        return ThemeResponse.from(savedTheme);
    }

    private void validateDuplicated(Theme theme) {
        boolean isDuplicatedName = themeRepository.findAll().stream()
                .anyMatch(theme::isDuplicated);
        if (isDuplicatedName) {
            throw new DuplicatedException("중복된 테마 이름입니다.");
        }
    }

    public List<ThemeResponse> readThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<ThemeResponse> readPopularThemes() {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(POPULAR_THEME_PERIOD);

        Map<Theme, Long> themeReferenceCount = calcThemeReferenceCount(start, end);
        return themeReferenceCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(POPULAR_THEME_COUNT)
                .map(e -> ThemeResponse.from(e.getKey()))
                .toList();
    }

    private Map<Theme, Long> calcThemeReferenceCount(LocalDate start, LocalDate end) {
        return reservationRepository.findByDateBetween(start, end).stream()
                .collect(Collectors.groupingBy(Reservation::getTheme, Collectors.counting()));
    }

    public ThemeResponse readTheme(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 테마입니다."));
        return ThemeResponse.from(theme);
    }

    public void deleteTheme(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new ReferencedReservationExistException("해당 테마에 예약이 존재합니다.");
        }
        themeRepository.deleteById(id);
    }
}
