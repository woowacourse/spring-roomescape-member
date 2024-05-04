package roomescape.service;

import static roomescape.exception.ExceptionType.DELETE_USED_THEME;
import static roomescape.exception.ExceptionType.DUPLICATE_THEME;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.domain.Themes;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.RoomescapeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ThemeResponse save(ThemeRequest themeRequest) {
        Themes themes = themeRepository.findAll();
        if (themes.hasNameOf(themeRequest.name())) {
            throw new RoomescapeException(DUPLICATE_THEME);
        }
        Theme saved = themeRepository.save(
                new Theme(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail()));
        return toResponse(saved);
    }

    private ThemeResponse toResponse(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll().getThemes().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ThemeResponse> findAndOrderByPopularity(LocalDate start, LocalDate end, int count) {
        return themeRepository.findAndOrderByPopularity(start, end, count).getThemes().stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(long id) {
        if (isUsedTheme(id)) {
            throw new RoomescapeException(DELETE_USED_THEME);
        }
        themeRepository.delete(id);
    }

    //todo SQL로 구현
    private boolean isUsedTheme(long themeId) {
        return reservationRepository.findAll().hasThemeOf(themeId);
    }
}
