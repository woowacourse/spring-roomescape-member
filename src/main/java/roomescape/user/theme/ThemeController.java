package roomescape.user.theme;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.user.theme.dto.ThemeResponse;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/top")
    public ResponseEntity<List<ThemeResponse>> getTopThemes() {
        List<ThemeResponse> responses = themeService.getTopThemes();
        return ResponseEntity.ok(responses);
    }
}
