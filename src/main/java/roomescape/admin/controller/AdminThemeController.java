package roomescape.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.admin.dto.AdminThemeRequest;
import roomescape.admin.dto.AdminThemeResponse;
import roomescape.admin.service.AdminThemeService;

import java.util.List;

@RestController
@RequestMapping("/admin/themes")
public class AdminThemeController {
    private final AdminThemeService adminThemeService;

    public AdminThemeController(AdminThemeService adminThemeService) {
        this.adminThemeService = adminThemeService;
    }

    @PostMapping
    public ResponseEntity<AdminThemeResponse> createTheme(
            @RequestBody AdminThemeRequest request
            ) {
        AdminThemeResponse response = adminThemeService.createTheme(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AdminThemeResponse>> getThemes() {
        List<AdminThemeResponse> responses = adminThemeService.getAllThemes();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(
            @PathVariable Long id
    ) {
        adminThemeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
