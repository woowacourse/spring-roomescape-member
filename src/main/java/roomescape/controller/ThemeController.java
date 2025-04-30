package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreationRequest;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<Theme> getReservationTimes() {
        return themeService.findAllTheme();
    }

    @PostMapping
    public ResponseEntity<Theme> createTheme(ThemeCreationRequest request) {
        long savedId = themeService.addTheme(request);
        Theme theme = themeService.findThemeById(savedId);
        return ResponseEntity.created(URI.create("theme/" + savedId)).body(theme);
    }
}
