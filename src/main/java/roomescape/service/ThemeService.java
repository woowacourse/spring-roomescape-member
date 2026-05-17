package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.dto.theme.ThemeRequest;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.theme.ThemeRepository;

@Service
@Transactional
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public Theme addTheme(ThemeRequest request) {
        Theme theme = new Theme(request.name(), request.description(), request.imageUrl());
        return themeRepository.createTheme(theme);
    }

    public void deleteThemeById(final long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new RoomEscapeException(ErrorCode.THEME_HAS_RESERVATIONS);
        }

        themeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Theme> findWeekPopularThemesOrderByRank(final int limit) {
        return themeRepository.findWeekPopularThemesOrderByRank(limit);
    }
}
