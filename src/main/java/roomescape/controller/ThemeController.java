package roomescape.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Theme;
import roomescape.dto.PopularThemeResponse;
import roomescape.dto.ReservationTimeStatusResponse;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public List<ThemeResponse> read() {
        return themeService.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @PostMapping("/admin/themes")
    @ResponseStatus(HttpStatus.CREATED)
    public ThemeResponse create(@RequestBody ThemeRequest themeRequest) {
        Theme theme = themeRequest.toEntity();
        Theme savedTheme = themeService.save(theme);
        return ThemeResponse.from(savedTheme);
    }

    @DeleteMapping("/admin/themes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        themeService.deleteById(id);
    }

    @GetMapping("/themes/{themeId}/reservation-times")
    public List<ReservationTimeStatusResponse> read(@RequestParam LocalDate date, @PathVariable Long themeId) {
        return themeService.findReservationTimeByDateAndThemeId(date, themeId);
    }

    @GetMapping("/themes/popular")
    public List<PopularThemeResponse> readPopularThemes(){
        return themeService.findPopularThemes();
    }
}
