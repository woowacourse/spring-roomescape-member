package roomescape.reservation.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;
import roomescape.reservation.dto.request.ThemeRequest;
import roomescape.reservation.dto.response.ThemeResponse;
import roomescape.reservation.service.ThemeService;

@Controller
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> postTheme(@RequestBody ThemeRequest themeRequest) {
        ThemeResponse themeResponse = themeService.createTheme(themeRequest);
        URI location = UriComponentsBuilder.newInstance()
                .path("/themes/{id}")
                .buildAndExpand(themeResponse.id())
                .toUri();

        return ResponseEntity.created(location).body(themeResponse);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<ThemeResponse> themes = themeService.findAllThemes();
        return ResponseEntity.ok(themes);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<ThemeResponse>> getWeeklyTopThemes() {
        List<ThemeResponse> weeklyTopThemes = themeService.findWeeklyTop10Themes();
        return ResponseEntity.ok(weeklyTopThemes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent()
                .build();
    }
}
