package roomescape.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.theme.dto.ThemeResponse;
import roomescape.service.ThemeService;

import java.util.List;

@RequestMapping("/themes")
@RestController
public class UserThemeRestController {

    private final ThemeService themeService;

    public UserThemeRestController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> readAll() {
        return ResponseEntity.ok().body(themeService.findAll());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> readPopularTheme(
            @RequestParam(defaultValue = "7") Integer period,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        return ResponseEntity.ok().body(themeService.findPopularTheme(period, limit));
    }
}

