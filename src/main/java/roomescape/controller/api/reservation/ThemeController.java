package roomescape.controller.api.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.ThemeAddRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

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
    public List<ThemeResponse> findThemes(@RequestParam(required = false) Integer limit) {
        if (limit != null) {
            return themeService.findPopularThemes(limit)
                    .stream()
                    .toList();
        }
        return themeService.findThemes();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
