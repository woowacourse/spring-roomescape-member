package roomescape.domain.theme.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.theme.dto.request.ThemeCreateRequestDTO;
import roomescape.domain.theme.dto.response.ThemeResponseDTO;
import roomescape.domain.theme.service.ThemeService;

@RestController
@RequestMapping("/api")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponseDTO>> getThemes() {
        return ResponseEntity.ok(themeService.getThemes());
    }

    @GetMapping("/themes/rankings/last-7-days")
    public ResponseEntity<List<ThemeResponseDTO>> getPopularThemes() {
        return ResponseEntity.ok(themeService.getPopularThemes());
    }

    @PostMapping("/admin/themes")
    public ResponseEntity<ThemeResponseDTO> saveTheme(@RequestBody ThemeCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(themeService.saveTheme(requestDTO));
    }

    @DeleteMapping("/admin/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteThemeById(id);
        return ResponseEntity.ok().build();
    }
}
