package roomescape.controller;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.service.ThemeService;

@RestController
public class ThemeController {
    private final ThemeService themeService;

    @Autowired
    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/themes")
    public ResponseEntity<Theme> create(@RequestBody Theme theme) {
        Theme createdTheme = themeService.save(theme);
        URI location = URI.create("/themes/" + createdTheme.getId());
        return ResponseEntity.created(location).body(createdTheme);
    }
}
