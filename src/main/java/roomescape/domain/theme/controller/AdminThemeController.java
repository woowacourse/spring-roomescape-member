package roomescape.domain.theme.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.theme.request.ThemeCreateRequest;
import roomescape.domain.theme.response.ThemeResponse;
import roomescape.domain.theme.service.ThemeService;

@RestController
@RequestMapping("/admin/themes")
public class AdminThemeController {

    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody @Valid ThemeCreateRequest request) {
        ThemeResponse theme = themeService.saveTheme(request);
        return ResponseEntity.created(URI.create("/themes/" + theme.id()))
                .body(theme);
    }

    // TODO: 관리자 테마 수정 기능 추가 put

    @DeleteMapping("/{themeId}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long themeId) {
        themeService.deleteThemeById(themeId);
        return ResponseEntity.noContent().build();
    }
}
