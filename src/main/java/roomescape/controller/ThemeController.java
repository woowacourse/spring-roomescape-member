package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.service.dto.SaveThemeRequest;
import roomescape.service.dto.ThemeResponse;
import roomescape.service.theme.ThemeCreateService;
import roomescape.service.theme.ThemeDeleteService;
import roomescape.service.theme.ThemeFindService;

import java.net.URI;
import java.util.List;

@RestController
public class ThemeController {

    private final ThemeCreateService themeCreateService;
    private final ThemeFindService themeFindService;
    private final ThemeDeleteService themeDeleteService;

    public ThemeController(ThemeCreateService themeCreateService, ThemeFindService themeFindService, ThemeDeleteService themeDeleteService) {
        this.themeCreateService = themeCreateService;
        this.themeFindService = themeFindService;
        this.themeDeleteService = themeDeleteService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<Theme> themes = themeFindService.findThemes();
        return ResponseEntity.ok(ThemeResponse.listOf(themes));
    }

    @GetMapping("/themes/ranks")
    public ResponseEntity<List<ThemeResponse>> getThemeRanks() {
        List<Theme> themes = themeFindService.findTop10Recent7Days();
        return ResponseEntity.ok(ThemeResponse.listOf(themes));
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> addTheme(@RequestBody SaveThemeRequest request) {
        Theme theme = themeCreateService.createTheme(request);
        return ResponseEntity.created(URI.create("/themes/" + theme.getId()))
                .body(ThemeResponse.of(theme));
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeDeleteService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
