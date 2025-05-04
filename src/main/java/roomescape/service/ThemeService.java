package roomescape.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.ThemeRepository;

@Service
public class ThemeService {
    // todo: Service naming 다시 생각해보기
    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse createTheme(ThemeCreateRequest request) {
        Theme saved = themeRepository.save(request.toTheme());
        return ThemeResponse.from(saved);
    }

    public List<ThemeResponse> findAll() {
        List<Theme> themes = themeRepository.findAll();
        return toThemeResponses(themes);
    }

    public void deleteThemeById(Long id) {
        themeRepository.deleteById(id);
    }

    public List<ThemeResponse> findLimitedThemesByPopularDesc(String orderType, Long listNum) {
        List<Theme> themes;
        if (orderType.equals("popular_desc")) {
            themes = themeRepository.findTopByReservationCountDesc(listNum);
            return toThemeResponses(themes);
        }

        themes = themeRepository.findTopByReservationCountDesc(listNum);
        return toThemeResponses(themes);
    }

    private List<ThemeResponse> toThemeResponses(List<Theme> themes) {
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
