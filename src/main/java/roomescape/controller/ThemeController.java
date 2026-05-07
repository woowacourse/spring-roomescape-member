package roomescape.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@RequestMapping("/themes")
@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ThemeResponse> getAllThemes() {
        return themeService.getAllThemes();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(params = "condition")
    public List<ThemeResponse> getThemesByCondition(
            @RequestParam String condition,
            @RequestParam(defaultValue = "10") int size) {
        if (!condition.equals("popular")){
            throw new IllegalArgumentException("Invalid condition: " + condition);
        }
        return themeService.getPopularThemes( size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/available-times")
    public List<AvailableReservationTimeResponse> getReservationTimes(
            @PathVariable long id,
            @RequestParam String date) {
        return themeService.getAvailableTimeResponses(id, date);
    }
}
