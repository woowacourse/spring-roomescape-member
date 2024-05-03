package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.reservation.dto.PopularThemeResponse;
import roomescape.controller.theme.dto.CreateThemeRequest;
import roomescape.controller.theme.dto.ThemeResponse;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.exception.ThemeUsedException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThemeService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ThemeService(final ReservationRepository reservationRepository, final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> getThemes() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse addTheme(final CreateThemeRequest createThemeRequest) {
        final Theme theme = createThemeRequest.toDomain();
        final Theme savedTheme = themeRepository.save(theme);
        return ThemeResponse.from(savedTheme);
    }

    public int deleteTheme(final Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new ThemeUsedException("예약된 테마는 삭제할 수 없습니다.");
        }
        return themeRepository.delete(id);
    }

    public List<PopularThemeResponse> getPopularThemes(final LocalDate today) {
        final List<Theme> reservations = reservationRepository.findPopularThemes(today);
        return reservations.stream()
                .map(PopularThemeResponse::from)
                .toList();
    }
}
