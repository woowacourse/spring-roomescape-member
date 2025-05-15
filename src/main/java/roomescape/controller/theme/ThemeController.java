package roomescape.controller.theme;

import jakarta.validation.Valid;
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
import roomescape.dto.theme.request.ThemeRequestDto;
import roomescape.dto.theme.response.ThemeResponseDto;
import roomescape.service.theme.ThemeService;

@RequestMapping("/themes")
@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/ranks")
    public List<ThemeResponseDto> readThemeRanks() {
        return themeService.getAllThemeOfRanks();
    }

    @GetMapping
    public List<ThemeResponseDto> readThemes() {
        return themeService.getAllThemes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ThemeResponseDto saveTheme(@RequestBody @Valid final ThemeRequestDto request) {
        return themeService.saveTheme(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheme(@PathVariable final Long id) {
        themeService.deleteTheme(id);
    }
}
