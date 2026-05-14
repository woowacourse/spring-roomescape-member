package roomescape.theme.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.AdminThemeRequest;
import roomescape.theme.dto.AdminThemeResponse;
import roomescape.theme.dto.AdminThemesResponse;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/admin")
public class AdminThemeController {
    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<AdminThemesResponse> readAll() {
        List<AdminThemeResponse> themes = themeService.findAll().stream()
                .map(AdminThemeResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new AdminThemesResponse(themes, themes.size()));
    }

    @GetMapping("/themes/{id}")
    public ResponseEntity<AdminThemeResponse> readById(@PathVariable Long id) {
        Theme theme = themeService.findById(id);
        AdminThemeResponse adminThemeResponse = AdminThemeResponse.from(theme);
        return ResponseEntity.ok(adminThemeResponse);
    }

    @PostMapping("/themes")
    public ResponseEntity<AdminThemeResponse> createTheme(@RequestBody AdminThemeRequest request) {
        Theme theme = themeService.addTheme(
                request.name(),
                request.description(),
                request.image());
        AdminThemeResponse adminThemeResponse = AdminThemeResponse.from(theme);
        return ResponseEntity.status(HttpStatus.CREATED).body(adminThemeResponse);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        themeService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
