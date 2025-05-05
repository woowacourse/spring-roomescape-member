package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreationRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ThemeService(final ReservationRepository reservationRepository, final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public long addTheme(ThemeCreationRequest request) {
        Theme theme = Theme.createWithoutId(request.name(), request.description(), request.thumbnail());
        return themeRepository.addTheme(theme);
    }

    public List<ThemeResponse> findAllTheme() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse findThemeById(long themeId) {
        Theme theme = loadThemeById(themeId);
        return ThemeResponse.from(theme);
    }

    public List<ThemeResponse> findTopThemes() {
        LocalDate endDate = LocalDate.now();
        List<Theme> topThemes = themeRepository.getTopThemesByCount(endDate.minusDays(7L), endDate);
        return topThemes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void deleteThemeById(long themeId) {
        validateThemeById(themeId);
        validateReservationExistenceInTheme(themeId);
        themeRepository.deleteById(themeId);
    }

    private Theme loadThemeById(long themeId) {
        Optional<Theme> theme = themeRepository.findById(themeId);
        return theme.orElseThrow(() -> new NotFoundException("[ERROR] ID에 해당하는 테마가 존재하지 않습니다."));
    }

    private void validateThemeById(long themeId) {
        Optional<Theme> theme = themeRepository.findById(themeId);
        if (theme.isEmpty()) {
            throw new NotFoundException("[ERROR] ID에 해당하는 테마가 존재하지 않습니다.");
        }
    }

    private void validateReservationExistenceInTheme(long themeId) {
        boolean isExist = reservationRepository.checkExistenceInTheme(themeId);
        if (isExist) {
            throw new BadRequestException("[ERROR] 예약이 이미 존재하는 테마를 제거할 수 없습니다.");
        }
    }
}
