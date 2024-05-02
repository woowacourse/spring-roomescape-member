package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.theme.CreateThemeRequest;
import roomescape.controller.theme.CreateThemeResponse;
import roomescape.domain.Theme;
import roomescape.service.exception.ThemeUsedException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

import java.util.List;

@Service
public class ThemeService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ThemeService(final ReservationRepository reservationRepository, final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public List<CreateThemeResponse> getThemes() {
        return themeRepository.findAll().stream()
                .map(CreateThemeResponse::from)
                .toList();
    }

    public CreateThemeResponse addTheme(final CreateThemeRequest createThemeRequest) {
        final Theme theme = createThemeRequest.toDomain();
        final Theme savedTheme = themeRepository.save(theme);
        return CreateThemeResponse.from(savedTheme);
    }

    public int deleteTheme(final Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new ThemeUsedException("예약된 테마는 삭제할 수 없습니다.");
        }
        return themeRepository.delete(id);
    }
}
