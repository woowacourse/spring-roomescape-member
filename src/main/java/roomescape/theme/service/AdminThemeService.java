package roomescape.theme.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.AdminThemeRequest;
import roomescape.theme.dto.AdminThemeResponse;
import roomescape.theme.repository.ThemeRepository;

@Service
public class AdminThemeService {

    private final ThemeRepository themeRepository;

    public AdminThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public AdminThemeResponse createTheme(AdminThemeRequest request) {
        Theme theme = Theme.of(request.name(), request.description(), request.imageUrl());
        Theme saved = themeRepository.save(theme);
        return AdminThemeResponse.from(saved);
    }

    public List<AdminThemeResponse> getAllThemes() {
        return themeRepository.findAll().stream()
                .map(AdminThemeResponse::from)
                .collect(Collectors.toList());
    }

    public void deleteTheme(Long id) {
        themeRepository.deleteById(id);
    }
}