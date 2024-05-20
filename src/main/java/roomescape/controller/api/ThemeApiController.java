package roomescape.controller.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.api.dto.response.ThemesResponse;
import roomescape.service.ThemeService;
import roomescape.service.dto.output.ThemeOutput;

@RestController
@RequestMapping("/themes")
public class ThemeApiController {

    private final ThemeService themeService;

    public ThemeApiController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<ThemesResponse> getAllThemes() {
        final List<ThemeOutput> outputs = themeService.getAllThemes();
        return ResponseEntity.ok(ThemesResponse.from(outputs));
    }

    @GetMapping("/popular")
    public ResponseEntity<ThemesResponse> findPopularThemes(
            @RequestParam final String date,
            @RequestParam(required = false, defaultValue = "10") final int limit) {
        final List<ThemeOutput> outputs = themeService.findPopularThemes(date, limit);
        return ResponseEntity.ok().body(ThemesResponse.from(outputs));
    }
}
