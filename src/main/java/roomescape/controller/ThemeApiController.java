package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ThemeService;
import roomescape.service.dto.ThemeRequestDto;
import roomescape.service.dto.ThemeResponseDto;

@RestController
public class ThemeApiController {

    private final ThemeService themeService;

    public ThemeApiController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public List<ThemeResponseDto> findAllThemes() {
        return themeService.findAllThemes();
    }

    @GetMapping("/themes/rank")
    public List<ThemeResponseDto> findTopBookedThemes() {
        return themeService.findTopBookedThemes();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/themes")
    public ThemeResponseDto createTheme(@RequestBody ThemeRequestDto requestDto) {
        return themeService.createTheme(requestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/themes/{id}")
    public void deleteTheme(@PathVariable long id) {
        themeService.deleteTheme(id);
    }
}
