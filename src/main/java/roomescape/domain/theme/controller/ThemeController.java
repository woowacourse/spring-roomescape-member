package roomescape.domain.theme.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.theme.dto.request.ThemeRequestDto;
import roomescape.domain.theme.dto.response.ThemeResponseDto;
import roomescape.domain.theme.service.ThemeService;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes/ranks")
    public List<ThemeResponseDto> readThemeRanks() {
        return themeService.getAllThemeOfRanks();
    }

    @GetMapping("/themes")
    public List<ThemeResponseDto> readThemes() {
        return themeService.getAllThemes();
    }

    @PostMapping("/themes")
    @ResponseStatus(HttpStatus.CREATED)
    public ThemeResponseDto saveTheme(@RequestBody ThemeRequestDto request) {
        return themeService.saveTheme(request);
    }

    @DeleteMapping("/themes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
    }
}
