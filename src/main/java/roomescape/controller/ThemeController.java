package roomescape.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.ThemeRequest;
import roomescape.controller.dto.ThemeResponse;
import roomescape.domain.Theme;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> themes() {
        return ResponseEntity.ok(convertToTimeResponses(themeService.allTheme()));
    }

    @GetMapping(params = {"topCount", "during"})
    public ResponseEntity<List<ThemeResponse>> popularThemes(
            @RequestParam("topCount") Long topCount,
            @RequestParam("during") Long during
    ) {
        return ResponseEntity.ok(convertToTimeResponses(themeService.findPopularThemes(topCount, during)));
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTime(@RequestBody ThemeRequest themeRequest) {
        Theme Theme = themeService.saveTime(themeRequest.name(), themeRequest.description(),
                themeRequest.thumbnailUrl());
        return ResponseEntity.ok(ThemeResponse.from(Theme));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable long id) {
        themeService.removeTime(id);
        return ResponseEntity.ok().build();
    }

    private List<ThemeResponse> convertToTimeResponses(List<Theme> themes) {
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
