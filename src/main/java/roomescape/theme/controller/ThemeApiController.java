package roomescape.theme.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.dto.PopularThemeResponse;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

@RestController
public class ThemeApiController {

    private final ThemeService themeService;

    public ThemeApiController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes/popular")
    public ResponseEntity<List<PopularThemeResponse>> findPopularThemeLimitTen() {
        List<PopularThemeResponse> popularThemeResponses = themeService.findPopularThemeLimitTen();
        return ResponseEntity.ok(popularThemeResponses);
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> findAll() {
        List<ThemeResponse> themeResponses = themeService.findAll();

        return ResponseEntity.ok(themeResponses);
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> save(@RequestBody ThemeRequest themeRequest) {
        Long saveId = themeService.save(themeRequest);
        ThemeResponse themeResponse = themeService.findById(saveId);

        return ResponseEntity.created(URI.create("/themes/" + saveId)).body(themeResponse);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
