package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
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

@RestController("/admin/themes")
public class ThemeController {
    private static final String DEFAULT_PATH = "/themes/";
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<ThemeResponse> themeResponses = ThemeResponse.from(themeService.getThemes());
        return ResponseEntity.ok(themeResponses);
    }

    public ResponseEntity<ThemeResponse> saveTheme(@RequestBody ThemeRequest themeRequest) {
        Theme theme = themeService.saveTheme(themeRequest.domain());
        ThemeResponse themeResponse = ThemeResponse.from(theme);

        return ResponseEntity.created(getLocation(themeResponse.id())).body(themeResponse);
    }

    @NonNull
    private static URI getLocation(long id) {
        return URI.create(DEFAULT_PATH + id);
    }
}
