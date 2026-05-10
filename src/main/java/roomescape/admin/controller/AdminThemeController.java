package roomescape.admin.controller;

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
import roomescape.admin.dto.AdminThemeRequest;
import roomescape.admin.dto.AdminThemeResponse;
import roomescape.admin.service.AdminThemeService;
import roomescape.domain.Theme;

@RestController
@RequestMapping("/admin")
public class AdminThemeController {
    private final AdminThemeService adminThemeService;

    public AdminThemeController(AdminThemeService adminThemeService) {
        this.adminThemeService = adminThemeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<AdminThemeResponse>> readAll() {
        List<AdminThemeResponse> themes = adminThemeService.findAll().stream()
                .map(AdminThemeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(themes);
    }

    @GetMapping("/themes/{id}")
    public ResponseEntity<AdminThemeResponse> readById(@PathVariable Long id) {
        Theme theme = adminThemeService.findById(id);
        AdminThemeResponse adminThemeResponse = AdminThemeResponse.from(theme);
        return ResponseEntity.ok(adminThemeResponse);
    }

    @PostMapping("/themes")
    public ResponseEntity<AdminThemeResponse> createTheme(@RequestBody AdminThemeRequest request) {
        Theme theme = adminThemeService.addTheme(
                request.name(),
                request.description(),
                request.image());
        AdminThemeResponse adminThemeResponse = AdminThemeResponse.from(theme);
        return ResponseEntity.status(HttpStatus.CREATED).body(adminThemeResponse);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        adminThemeService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
