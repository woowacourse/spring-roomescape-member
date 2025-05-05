package roomescape.controller;

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
import roomescape.dto.BookedReservationTimeResponseDto;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;
import roomescape.service.ThemeServiceImpl;

@RestController
public class ThemeController {

    private final ThemeServiceImpl themeServiceImpl;

    public ThemeController(ThemeServiceImpl themeServiceImpl) {
        this.themeServiceImpl = themeServiceImpl;
    }

    @GetMapping("/themes/ranks")
    public List<ThemeResponseDto> readThemeRanks() {
        return themeServiceImpl.getAllThemeOfRanks();
    }

    @GetMapping("/themes/{themeId}/times")
    public List<BookedReservationTimeResponseDto> readBookedReservationTimes(
        @RequestParam String date, @PathVariable Long themeId) {
        return themeServiceImpl.getAllBookedReservationTimes(date, themeId);
    }

    @GetMapping("/themes")
    public List<ThemeResponseDto> readThemes() {
        return themeServiceImpl.getAllThemes();
    }

    @PostMapping("/themes")
    @ResponseStatus(HttpStatus.CREATED)
    public ThemeResponseDto saveTheme(@RequestBody ThemeRequestDto request) {
        return themeServiceImpl.saveTheme(request);
    }

    @DeleteMapping("/themes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheme(@PathVariable Long id) {
        themeServiceImpl.deleteTheme(id);
    }
}
