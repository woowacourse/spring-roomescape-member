package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(final ThemeRepository themeRepository, final ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse createTheme(final ThemeRequest themeRequest) {
        Theme theme = themeRequest.convertToTheme();
        if (themeRepository.existsByName(theme.getThemeName())) {
            throw new IllegalArgumentException("해당 이름의 테마는 이미 존재합니다.");
        }
        final Theme savedTheme = themeRepository.save(theme);
        return new ThemeResponse(savedTheme);
    }

    public List<ThemeResponse> findAll() {
        final List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public void deleteThemeById(final Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new IllegalArgumentException("예약이 존재하는 테마는 삭제할 수 없습니다.");
        }
        themeRepository.deleteById(id);
    }

    public List<ThemeResponse> findPopularThemes() {
        final LocalDate from = LocalDate.now().minusDays(7);
        final LocalDate to = LocalDate.now().minusDays(1);
        final List<Theme> popularThemes = themeRepository.findPopularThemes(from, to, 10);
        return popularThemes.stream()
                .map(ThemeResponse::new)
                .toList();
    }
}
