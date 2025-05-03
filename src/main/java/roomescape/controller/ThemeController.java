package roomescape.controller;

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
import roomescape.application.dto.ThemeRequest;
import roomescape.application.dto.ThemeResponse;
import roomescape.application.service.ThemeService;

@RestController
@RequestMapping("themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<ThemeResponse> readThemes() {
        return themeService.findAllThemes();
    }

    @GetMapping("rank")
    public List<ThemeResponse> readThemeRank() {
        return themeService.findThemeRank();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ThemeResponse createTheme(
            @RequestBody ThemeRequest request
    ) {
        return themeService.createTheme(request);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheme(
            @PathVariable Long id
    ) {
        themeService.deleteTheme(id);
    }
}
