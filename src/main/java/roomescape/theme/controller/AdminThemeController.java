package roomescape.theme.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.dto.request.AdminThemePageResponse;
import roomescape.theme.dto.request.ThemeRequest;
import roomescape.theme.dto.response.CreateThemeResponse;
import roomescape.theme.service.ThemeService;

@RequestMapping("/admin/themes")
@RestController
public class AdminThemeController {

    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<CreateThemeResponse> addTheme(@RequestBody @Valid ThemeRequest request) {
        CreateThemeResponse themeResponse = themeService.addTheme(request);
        return ResponseEntity.created(URI.create("/themes/" + themeResponse.id())).body(themeResponse);
    }

    @GetMapping
    public ResponseEntity<AdminThemePageResponse> getThemesWithPage(
            @RequestParam(required = false, defaultValue = "1") int page) {
        return ResponseEntity.ok(themeService.getThemesByPage(page));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable(value = "id") Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
