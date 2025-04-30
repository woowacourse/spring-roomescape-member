package roomescape.theme.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.PopularThemeResponse;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;

@Service
public class ThemeService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository,
                        ReservationRepository reservationRepository) {

        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse add(ThemeRequest themeRequest) {
        validateNoDuplicateTheme(themeRequest.name());

        Theme theme = new Theme(null, themeRequest.name(),
                themeRequest.description(), themeRequest.thumbnail());

        return ThemeResponse.from(themeRepository.add(theme));
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public List<PopularThemeResponse> findTop10MostReservedLastWeek() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now().minusDays(1);

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
