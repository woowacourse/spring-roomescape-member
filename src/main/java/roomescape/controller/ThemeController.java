package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreateRequest;
import roomescape.dto.ThemeDeleteRequest;
import roomescape.service.ThemeService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody ThemeCreateRequest request) {
        Theme theme = themeService.createTheme(
                request.name(),
                request.description(),
                request.imgUrl(),
                request.userName()
        );
        return ResponseEntity.created(URI.create("/api/v1/themes/" + theme.getId())).body(theme);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id,
                                            @RequestBody ThemeDeleteRequest request) {
        themeService.deleteTheme(id, request.userName());
        return ResponseEntity.noContent().build();
    }
}
