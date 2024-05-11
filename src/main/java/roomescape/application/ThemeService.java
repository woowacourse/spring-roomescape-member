package roomescape.application;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.request.ThemeCreationRequest;
import roomescape.application.dto.response.ThemeResponse;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;

@Service
public class ThemeService {
    private final Clock clock;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(Clock clock, ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.clock = clock;
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse save(ThemeCreationRequest request) {
        Theme theme = themeRepository.save(request.toTheme());
        return ThemeResponse.from(theme);
    }

    public List<ThemeResponse> findThemes() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void delete(long id) {
        validateReservedTheme(id);
        boolean isDeleted = themeRepository.deleteById(id);
        if (!isDeleted) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다.");
        }
    }

    private void validateReservedTheme(long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new IllegalArgumentException("해당 테마를 사용하는 예약이 존재합니다.");
        }
    }

    public List<ThemeResponse> findPopularThemes() {
        return themeRepository.findPopularThemesForWeekLimit10(LocalDate.now(clock))
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
