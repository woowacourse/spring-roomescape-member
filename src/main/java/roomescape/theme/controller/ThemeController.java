package roomescape.theme.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.controller.dto.ThemeRankingResponse;
import roomescape.theme.controller.dto.ThemeRequest;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

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

    @GetMapping("/ranking")
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
