package roomescape.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ThemeRequest;
import roomescape.service.AdminThemeService;

@RequestMapping("/admin/themes")
@RestController
public class AdminThemeController {
    private final AdminThemeService adminThemeService;

    public AdminThemeController(AdminThemeService adminThemeService) {
        this.adminThemeService = adminThemeService;
    }

    @PostMapping
    public ResponseEntity<Void> createTheme(@RequestBody ThemeRequest themeRequest) {
        final long themeId = adminThemeService.save(themeRequest.name(), themeRequest.description(), themeRequest.thumbnailUrl());
        final URI location = URI.create("/themes/" + themeId);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable long id) {
        adminThemeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}