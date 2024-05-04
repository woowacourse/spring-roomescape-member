package roomescape.controller.reservation;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.ThemeAddRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> addTheme(
            @RequestBody ThemeAddRequest themeAddRequest) {
        ThemeResponse themeResponse = themeService.addTheme(themeAddRequest);
        return ResponseEntity.created(URI.create("/times/" + themeResponse.id()))
                .body(themeResponse);
    }

    @GetMapping
    public List<ThemeResponse> findThemes() {
        return themeService.findThemes();
    }

    @GetMapping("/trending")
    public List<ThemeResponse> findTrendingThemes(@RequestParam Long limit) {
        return themeService.findTrendingThemes(limit)
                .stream()
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
