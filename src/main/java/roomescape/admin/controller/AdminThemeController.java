
package roomescape.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.service.AdminThemeService;
import roomescape.theme.controller.dto.ThemeRequest;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.doamin.Theme;

@RequestMapping("/admin/themes")
@RestController
@RequiredArgsConstructor
public class AdminThemeController {
    private final AdminThemeService adminThemeService;

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody @Valid ThemeRequest request) {
        final Theme theme = adminThemeService.saveTheme(request.name(), request.description(), request.thumbnailUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(ThemeResponse.from(theme));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable long id) {
        adminThemeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
