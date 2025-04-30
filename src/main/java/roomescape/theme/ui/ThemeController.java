package roomescape.theme.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.uri.UriFactory;
import roomescape.theme.application.ThemeService;
import roomescape.theme.domain.ThemeId;
import roomescape.theme.ui.dto.CreateThemeWebRequest;
import roomescape.theme.ui.dto.ThemeResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(ThemeController.BASE_PATH)
@RequiredArgsConstructor
public class ThemeController {

    public static final String BASE_PATH = "/themes";

    private final ThemeService themeService;

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAll() {
        return ResponseEntity.ok(themeService.getAll());
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(@RequestBody final CreateThemeWebRequest createThemeWebRequest) {
        final ThemeResponse themeResponse = themeService.create(createThemeWebRequest);
        final URI location = UriFactory.buildPath(BASE_PATH, String.valueOf(themeResponse.id()));
        return ResponseEntity.created(location)
                .body(themeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        themeService.delete(ThemeId.from(id));
        return ResponseEntity.noContent().build();
    }
}
