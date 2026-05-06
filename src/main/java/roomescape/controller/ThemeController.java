package roomescape.controller;

import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Theme;
import roomescape.dto.theme.PopularThemeResponseDto;
import roomescape.dto.theme.PopularThemesResponseDto;
import roomescape.dto.theme.ThemeRequestDto;
import roomescape.dto.theme.ThemeResponseDto;
import roomescape.service.ThemeService;

import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ThemeResponseDto addTheme(@RequestBody ThemeRequestDto request) {
        Theme saved = themeService.addTheme(request);
        return ThemeResponseDto.from(saved);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheme(@PathVariable Long id) {
        themeService.deleteThemeById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ThemeResponseDto> findAll() {
        return themeService.getThemes().stream()
                .map(ThemeResponseDto::from)
                .toList();
    }

    @GetMapping("/popular/week")
    @ResponseStatus(HttpStatus.OK)
    public PopularThemesResponseDto findWeekPopularThemesOrderByRank(
        @RequestParam("limit") final int limit
    ) {
        List<Theme> themes = themeService.findWeekPopularThemesOrderByRank(limit);
        return PopularThemesResponseDto.from(themes);
    }
}
