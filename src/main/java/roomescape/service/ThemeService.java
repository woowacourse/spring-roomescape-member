package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeSaveRequest;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private static final int POPULAR_THEME_COUNT = 10;

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

    public void deleteTheme(final Long id) {
        final boolean isReservationExist = reservationRepository.existByThemeId(id);
        if (isReservationExist) {
            throw new IllegalArgumentException(String.format("예약이 존재하는 테마는 삭제할 수 없습니다. (%d)", id));
        }
        themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 테마입니다. (%d)", id)));

        themeRepository.deleteById(id);
    }

    public List<ThemeResponse> getPopularThemes(final LocalDate localDate) {
        return reservationRepository.findThemesOrderedByReservationCountForWeek(localDate, POPULAR_THEME_COUNT)
                .stream()
                .map(ThemeResponse::new)
                .toList();
    }
}
