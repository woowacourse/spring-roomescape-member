package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
            Long topCount,
            Long during
    ) {
        return ResponseEntity.ok(convertToTimeResponses(themeService.findPopularThemes(topCount, during)));
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTime(@RequestBody ThemeRequest themeRequest) {
        Theme Theme = themeService.saveTheme(themeRequest.name(), themeRequest.description(),
                themeRequest.thumbnailUrl());
        return ResponseEntity.ok(ThemeResponse.from(Theme));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable long id) {
        themeService.removeTheme(id);
        return ResponseEntity.ok().build();
    }

    private List<ThemeResponse> convertToTimeResponses(List<Theme> themes) {
        return themes.stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
