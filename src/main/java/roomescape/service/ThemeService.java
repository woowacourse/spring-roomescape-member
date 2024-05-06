package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.CustomException;

import static roomescape.exception.CustomExceptionCode.CAN_NOT_DELETE_THEME_DUE_TO_RESERVATION_EXIST;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ThemeResponse> getAllThemes() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse addTheme(ThemeRequest request) {
        Theme theme = request.toTheme();
        Theme savedTheme = themeRepository.save(theme);

        return ThemeResponse.from(savedTheme);
    }

    public void deleteThemeById(Long id) {
        boolean exist = reservationRepository.existByThemeId(id);
        if (exist) {
            throw new CustomException(CAN_NOT_DELETE_THEME_DUE_TO_RESERVATION_EXIST);
        }

        themeRepository.deleteById(id);
    }

    public List<ThemeResponse> getMostReservedThemes() {
        List<Theme> themes = themeRepository.findMostReservedThemesWithinDays(7, 10);

        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
