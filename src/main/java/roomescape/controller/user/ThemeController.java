package roomescape.controller.user;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeQueryService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeQueryService themeQueryService;

    public ThemeController(ThemeQueryService themeQueryService) {
        this.themeQueryService = themeQueryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ThemeResponse> readThemes() {
        return themeQueryService.findAllThemes();
    }

    @GetMapping("/rank")
    @ResponseStatus(HttpStatus.OK)
    public List<ThemeResponse> readThemeRank() {
        return themeQueryService.findThemeRank();
    }
}
