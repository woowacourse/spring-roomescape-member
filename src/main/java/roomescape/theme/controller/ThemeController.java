package roomescape.theme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RoleRequired;
import roomescape.member.entity.Role;
import roomescape.theme.dto.PopularThemeResponse;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
public class ThemeController {

    private final ThemeService service;

    public ThemeController(ThemeService service) {
        this.service = service;
    }

    @PostMapping("/themes")
    @RoleRequired(Role.ADMIN)
    public ResponseEntity<ThemeResponse> postTheme(@RequestBody ThemeRequest request) {
        ThemeResponse themeResponse = service.createTheme(request);
        URI location = URI.create("/themes/" + themeResponse.id());
        return ResponseEntity.created(location).body(themeResponse);
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        return ResponseEntity.ok(service.readThemes());
    }

    @DeleteMapping("/themes/{id}")
    @RoleRequired(Role.ADMIN)
    public ResponseEntity<Void> deleteTheme(@PathVariable long id) {
        service.deleteThemeById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/themes/popular")
    public ResponseEntity<List<PopularThemeResponse>> getRecentPopularThemes(@RequestParam(value = "size", defaultValue = "10") int count) {
        return ResponseEntity.ok(service.readRecentPopularThemes(count));
    }
}
