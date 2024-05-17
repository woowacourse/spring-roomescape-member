package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.theme.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
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

    public Long addTheme(ThemeRequest themeRequest) {
        validateNameDuplicate(themeRequest.name());
        Theme theme = themeRequest.toEntity();
        return themeRepository.save(theme);
    }

    public List<ThemeResponse> getAllTheme() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse getTheme(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 id가 존재하지 않습니다."));
        return ThemeResponse.from(theme);
    }

    public void deleteTheme(Long id) {
        themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 id가 존재하지 않습니다."));
        if (reservationRepository.existsByThemeId(id)) {
            throw new IllegalArgumentException("예약이 되어있어 해당 테마를 지울 수 없습니다.");
        }
        themeRepository.delete(id);
    }

    public List<ThemeResponse> getPopularThemes() {
        List<Long> popularThemeIds = reservationRepository.findPopularThemesByReservation(7, 1, 10);
        return popularThemeIds.stream()
                .map(themeRepository::findById)
                .map(optional -> optional.orElseThrow(() -> new IllegalArgumentException("해당하는 id가 존재하지 않습니다.")))
                .map(ThemeResponse::from)
                .toList();
    }

    public void validateNameDuplicate(String name) {
        if (themeRepository.existsByName(name)) {
            throw new IllegalArgumentException("동일한 이름이 존재합니다. : " + name);
        }
    }
}
