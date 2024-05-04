package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.dto.PopularThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeSaveRequest;
import roomescape.exception.ReservationBusinessException;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(final ThemeRepository themeRepository, final ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse saveTheme(final ThemeSaveRequest themeSaveRequest) {
        final Theme theme = themeSaveRequest.toTheme();
        final Theme savedTheme = themeRepository.save(theme);
        return new ThemeResponse(savedTheme);
    }

    public List<ThemeResponse> getThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public List<ThemeResponse> getPopularThemes(final PopularThemeRequest popularThemeRequest) {
        return reservationRepository.findTopThemesDurationOrderByCount(popularThemeRequest.startDate(), popularThemeRequest.endDate(), popularThemeRequest.limit())
                .stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public void deleteTheme(final Long id) {
        validateDeleteTheme(id);

        themeRepository.deleteById(id);
    }

    private void validateDeleteTheme(final Long id) {
        if (reservationRepository.existByThemeId(id)) {
            throw new ReservationBusinessException("예약이 존재하는 테마입니다.");
        }

        if (themeRepository.findById(id).isEmpty()) {
            throw new ReservationBusinessException("존재하지 않는 테마입니다.");
        }
    }
}
