package roomescape.domain.theme.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.theme.dto.response.ThemeResponseDTO;
import roomescape.domain.theme.service.ThemeService;

@RestController
@RequestMapping("/api")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/admin/themes")
    public ResponseEntity<List<ThemeResponseDTO>> getThemes() {
        return ResponseEntity.ok(themeService.getThemes());
    }
}
