package roomescape.theme.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.error.ReservationException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.repository.ThemeRepository;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeResponse saveTheme(final ThemeRequest request) {
        final Theme theme = themeRepository.save(new Theme(request.name(), request.description(), request.thumbnail()));
        return new ThemeResponse(theme);
    }

    public List<ThemeResponse> findAll() {
        final List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(ThemeResponse::new)
                .toList();
    }

    public void delete(final Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new ReservationException("해당 테마로 예약된 건이 존재합니다.");
        }

        themeRepository.deleteById(id);
    }
}

