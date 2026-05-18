package roomescape.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.ThemeCreateRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/admin/themes")
public class AdminThemeController {
    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@Valid @RequestBody ThemeCreateRequest request) {
        ThemeResponse themeResponse = themeService.createTheme(request);
        return ResponseEntity.created(URI.create("/api/v1/admin/themes/" + themeResponse.id())).body(themeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable @NotNull(message = "테마 ID는 필수로 입력해야 합니다.") Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
