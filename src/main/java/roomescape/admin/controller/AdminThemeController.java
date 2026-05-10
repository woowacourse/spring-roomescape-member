
package roomescape.admin.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.service.AdminThemeService;
import roomescape.theme.controller.dto.ThemeRequest;

@RequestMapping("/admin/themes")
@RestController
@RequiredArgsConstructor
public class AdminThemeController {
    private final AdminThemeService adminThemeService;

    @PostMapping
    public ResponseEntity<Void> createTheme(@RequestBody ThemeRequest request) {
        final long themeId = adminThemeService.saveTheme(request.name(), request.description(), request.thumbnailUrl());
        return ResponseEntity.created(URI.create("/themes/" + themeId)).build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTheme(@PathVariable long id) {
        adminThemeService.deleteTheme(id);
    }
}
