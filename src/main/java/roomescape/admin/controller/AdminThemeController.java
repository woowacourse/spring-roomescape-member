package roomescape.admin.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.dto.AdminThemeRequest;
import roomescape.admin.dto.AdminThemeResponse;
import roomescape.admin.service.AdminThemeService;

import java.util.List;

@RestController
public class AdminThemeController {

    private final AdminThemeService adminThemeService;

    public AdminThemeController(AdminThemeService adminThemeService) {
        this.adminThemeService = adminThemeService;
    }

    @PostMapping("/admin/themes")
    public ResponseEntity<AdminThemeResponse> createTheme(
        @RequestBody @Valid AdminThemeRequest request
    ) {
        AdminThemeResponse response = adminThemeService.createTheme(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/themes")
    public ResponseEntity<List<AdminThemeResponse>> getThemes() {
        List<AdminThemeResponse> responses = adminThemeService.getAllThemes();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/admin/themes/{id}")
    public ResponseEntity<Void> deleteTheme(
        @PathVariable Long id
    ) {
        adminThemeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
