package roomescape.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ThemeService;
import roomescape.service.dto.PopularThemeRequestDto;
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

    @GetMapping("/themes/popular")
    public List<ThemeResponseDto> findTopBookedThemes(@RequestParam(name = "start-date") String startDate,
                                                      @RequestParam(name = "end-date") String endDate,
                                                      @RequestParam Integer count) {
        return themeService.findTopBookedThemes(new PopularThemeRequestDto(startDate, endDate, count));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/themes")
    public ThemeResponseDto createTheme(@Valid @RequestBody ThemeRequestDto requestDto) {
        return themeService.createTheme(requestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/themes/{id}")
    public void deleteTheme(@PathVariable long id) {
        themeService.deleteTheme(id);
    }
}
