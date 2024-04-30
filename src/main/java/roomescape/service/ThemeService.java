package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreateRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.BadRequestException;
import roomescape.repository.ThemeRepository;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse createTheme(ThemeCreateRequest request) {
        Theme theme = request.toTheme();
        Theme savedTheme = themeRepository.save(theme);
        return ThemeResponse.from(savedTheme);
    }

    public ThemeResponse readTheme(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 테마입니다."));
        return ThemeResponse.from(theme);
    }

    public List<ThemeResponse> readThemes() {
        return  themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
