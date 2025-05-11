package roomescape.theme.application;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.infrastructure.ReservationRepository;
import roomescape.theme.infrastructure.ThemeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.PopularThemeResponse;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;

@Service
public class ThemeService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ThemeService(ThemeRepository themeRepository,
                        ReservationRepository reservationRepository,
                        Clock clock) {

        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
        this.clock = clock;
    }

    public ThemeResponse add(ThemeRequest request) {
        validateNoDuplicateTheme(request.name());

        Theme theme = Theme.createBeforeSaved(request.name(), request.description(), request.thumbnail());
        return ThemeResponse.from(themeRepository.add(theme));
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<PopularThemeResponse> findTop10MostReservedLastWeek() {
        LocalDate today = LocalDate.now(clock);
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today.minusDays(1);

        List<Theme> themes = themeRepository.findTop10MostReservedLastWeek(startDate, endDate);

        return themes.stream()
                .map(PopularThemeResponse::from)
                .toList();
    }

    public void deleteById(Long id) {
        ensureThemeExists(id);
        validateThemeNotInReservation(id);

        themeRepository.deleteById(id);
    }

    private void ensureThemeExists(Long id) {
        themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 테마 id가 존재하지 않습니다."));
    }

    private void validateNoDuplicateTheme(String name) {
        if (themeRepository.existsByName(name)) {
            throw new IllegalArgumentException("[ERROR] 이미 테마가 존재합니다.");
        }
    }

    private void validateThemeNotInReservation(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new IllegalArgumentException("[ERROR] 예약된 테마의 id는 삭제할 수 없습니다.");
        }
    }
}
