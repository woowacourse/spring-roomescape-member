package roomescape.theme.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.controller.dto.ThemeRankingResponse;
import roomescape.theme.controller.dto.ThemeRequest;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<ThemeResponse> getThemes() {
        return themeService.getThemes();
    }

    @GetMapping("/rankings")
    public List<ThemeRankingResponse> getThemeRankings() {
        return themeService.getThemeRankings();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ThemeResponse addTheme(@RequestBody ThemeRequest request) {
        return themeService.add(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{themeId}")
    public void deleteTheme(@PathVariable("themeId") Long themeId) {
        themeService.remove(themeId);
    }

}
