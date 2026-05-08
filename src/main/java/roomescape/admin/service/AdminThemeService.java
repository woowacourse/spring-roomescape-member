package roomescape.admin.service;

import org.springframework.stereotype.Service;
import roomescape.admin.domain.Theme;
import roomescape.admin.dto.AdminThemeRequest;
import roomescape.admin.dto.AdminThemeResponse;
import roomescape.admin.repository.AdminThemeRepository;

import java.util.List;
import java.util.stream.Collectors;

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
        return adminThemeRepository.findAll().stream()
                .map(AdminThemeResponse::from)
                .collect(Collectors.toList());
    }

    public void deleteTheme(Long id) {
        adminThemeRepository.deleteById(id);
    }
}
