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
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ThemeResponseDto> readThemes() {
        return themeService.findAllThemes();
    }

    @GetMapping("rank")
    @ResponseStatus(HttpStatus.OK)
    public List<ThemeResponseDto> readThemeRank() {
        return themeService.findThemeRank();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ThemeResponseDto createTheme(
            @RequestBody ThemeRequestDto request
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
