package roomescape.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.request.ThemeRequest;
import roomescape.response.ThemeResponse;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/themes")
public class AdminThemeController {
    private static final String DEFAULT_PATH = "/themes/";
    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<ThemeResponse> themeResponses = ThemeResponse.from(themeService.getThemes());

        return ResponseEntity.ok(themeResponses);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> saveTheme(@RequestBody ThemeRequest themeRequest) {
        Theme theme = themeService.saveTheme(themeRequest.toDomainForSave());
        ThemeResponse themeResponse = ThemeResponse.from(theme);

        return ResponseEntity.created(getLocation(themeResponse.id())).body(themeResponse);
    }

    @NonNull
    private static URI getLocation(long id) {
        return URI.create(DEFAULT_PATH + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);

        return ResponseEntity.noContent().build();
    }
}
