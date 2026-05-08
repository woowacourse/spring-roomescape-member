package roomescape.admin.service;

import org.springframework.stereotype.Service;
import roomescape.admin.domain.Theme;
import roomescape.admin.dto.AdminThemeRequest;
import roomescape.admin.dto.AdminThemeResponse;
import roomescape.admin.repository.AdminThemeRepository;

import java.util.List;

@Service
public class AdminThemeService {
    private final AdminThemeRepository adminThemeRepository;

    public AdminThemeService(AdminThemeRepository adminThemeRepository) {
        this.adminThemeRepository = adminThemeRepository;
    }

    public AdminThemeResponse createTheme(AdminThemeRequest request) {
        Theme theme = Theme.of(
                request.name(),
                request.description(),
                request.imageUrl()
        );
        Theme saved = adminThemeRepository.save(theme);
        return AdminThemeResponse.from(saved);
    }

    public List<AdminThemeResponse> getAllThemes() {
        List<Theme> themes = adminThemeRepository.findAll();

        return themes.stream()
            .map(AdminThemeResponse::from)
            .toList();
    }

    public void deleteTheme(Long id) {
        adminThemeRepository.deleteById(id);
    }
}
