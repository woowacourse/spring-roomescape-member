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
        validateIdExist(id);
        Theme theme = themeRepository.findById(id);
        return ThemeResponse.from(theme);
    }

    public void deleteTheme(Long id) {
        validateIdExist(id);
        if (reservationRepository.existsByThemeId(id)) {
            throw new IllegalArgumentException("해당 테마는 예약이 존재합니다.");
        }
        themeRepository.delete(id);
    }

    public List<ThemeResponse> getPopularThemes() {
        List<Long> popularThemeIds = reservationRepository.findThemeReservationCountsForLastWeek(7, 1, 10);
        return popularThemeIds.stream()
                .map(themeRepository::findById)
                .map(ThemeResponse::from)
                .toList();
    }

    private void validateIdExist(Long id) {
        if (!themeRepository.existsById(id)) {
            throw new IllegalArgumentException("id가 존재하지 않습니다 : " + id);
        }
    }

    public void validateNameDuplicate(String name) {
        if (themeRepository.existsByName(name)) {
            throw new IllegalArgumentException("동일한 이름이 존재합니다. : " + name);
        }
    }
}
