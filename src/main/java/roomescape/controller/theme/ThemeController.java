package roomescape.controller.theme;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<ThemeResponse> getThemes() {
        return themeService.getThemes();
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> addTheme(@RequestBody final ThemeRequest themeRequest) {
        final ThemeResponse theme = themeService.addTheme(themeRequest);
        URI uri = UriComponentsBuilder.fromPath("/themes/{id}")
                .buildAndExpand(theme.id())
                .toUri();

        return ResponseEntity.created(uri)
                .body(theme);
    }
}
