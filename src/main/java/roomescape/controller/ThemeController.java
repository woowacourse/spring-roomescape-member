package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.request.ThemeRequest;
import roomescape.model.Theme;
import roomescape.service.ThemeService;

import javax.xml.stream.Location;
import java.net.URI;
import java.net.URL;
import java.util.List;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<Theme>> getThemes() {
        return ResponseEntity.ok(themeService.findAllThemes());
    }

    @PostMapping("/themes")
    public ResponseEntity<Theme> addTheme(@RequestBody ThemeRequest themeRequest) {
        Theme theme = themeService.addTheme(themeRequest);
        return ResponseEntity
                .created(URI.create("/themes/" + theme.getId()))
                .body(theme);
    }
}
