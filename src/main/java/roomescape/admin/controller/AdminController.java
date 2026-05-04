package roomescape.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.dto.RequestTheme;
import roomescape.theme.dto.ResponseTheme;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ThemeService themeService;

    public AdminController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/themes")
    public ResponseTheme createTheme(@RequestBody RequestTheme requestTheme) {
        return ResponseTheme.from(themeService.createTheme(requestTheme.name(), requestTheme.description(), requestTheme.thumbnail()));
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> removeTheme(@PathVariable Long id) {
        themeService.removeTheme(id);
        return ResponseEntity.noContent().build();
    }
}
