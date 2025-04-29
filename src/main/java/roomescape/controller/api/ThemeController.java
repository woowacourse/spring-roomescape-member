package roomescape.controller.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.request.CreateThemeRequest;
import roomescape.controller.dto.response.ThemeResponse;
import roomescape.service.ThemeService;
import roomescape.service.dto.ThemeCreation;

@RestController
@RequestMapping("/theme")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> addTheme(@RequestBody CreateThemeRequest request) {
        ThemeResponse response = themeService.addTheme(
                new ThemeCreation(request.name(), request.description(), request.thumbnail()));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<ThemeResponse> findAllThemes() {
        return themeService.findAllThemes();
    }
}
