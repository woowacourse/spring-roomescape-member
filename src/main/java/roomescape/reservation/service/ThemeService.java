package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ThemeRepository;
import roomescape.reservation.dto.ThemeRequest;
import roomescape.reservation.dto.ThemeResponse;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;


    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> findAllThemes() {
        return themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public ThemeResponse create(ThemeRequest themeRequest) {
        Theme theme = new Theme(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        return ThemeResponse.from(themeRepository.save(theme));
    }

    public void delete(long themeId) {
        if (!themeRepository.deleteById(themeId)) {
            throw new IllegalArgumentException(String.format("테마 삭제에 실패했습니다. id=%d를 확인해주세요.", themeId));
        }
    }

    public List<ThemeResponse> findPopularThemes() {
        return themeRepository.findPopularThemes().stream()
                .map(ThemeResponse::from)
                .toList();

    }
}
