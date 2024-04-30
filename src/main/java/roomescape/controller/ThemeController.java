package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.service.dto.SaveThemeRequest;
import roomescape.service.dto.ThemeResponse;
import roomescape.service.theme.ThemeCreateService;
import roomescape.service.theme.ThemeFindService;

import java.net.URI;
import java.util.List;

@RestController
public class ThemeController {

    private final ThemeCreateService themeCreateService;
    private final ThemeFindService themeFindService;

    public ThemeController(ThemeCreateService themeCreateService, ThemeFindService themeFindService) {
        this.themeCreateService = themeCreateService;
        this.themeFindService = themeFindService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> getReservationTimes() {
        List<Theme> themes = themeFindService.findThemes();
        return ResponseEntity.ok(ThemeResponse.listOf(themes));
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> addTheme(@RequestBody SaveThemeRequest request) {
        Theme theme = themeCreateService.createTheme(request);
        return ResponseEntity.created(URI.create("/themes/" + theme.getId()))
                .body(ThemeResponse.of(theme));
    }
}
