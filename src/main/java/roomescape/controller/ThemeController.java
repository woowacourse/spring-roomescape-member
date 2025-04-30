package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.controller.dto.ThemeResponse;
import roomescape.service.ThemeService;

@Controller
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService service;

    public ThemeController(final ThemeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> allThemes() {
        var themes = service.allThemes();
        var response = ThemeResponse.from(themes);
        return ResponseEntity.ok(response);
    }
}
