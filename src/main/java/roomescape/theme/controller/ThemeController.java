package roomescape.theme.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/theme")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Theme>> read(
            @RequestParam final int period,
            @RequestParam final int limit
    ) {
        return ResponseEntity.ok()
                .body(themeService.getPopularThemes(period, limit));
    }

}
