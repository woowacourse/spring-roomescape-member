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
import roomescape.reservation.domain.Theme;
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
        Theme theme = themeService.createTheme(themeRequest.toEntity());
        URI location = UriComponentsBuilder.newInstance()
                .path("/themes/{id}")
                .buildAndExpand(theme.getId())
                .toUri();

        return ResponseEntity.created(location).body(ThemeResponse.from(theme));
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<ThemeResponse> themeResponses = themeService.findAllThemes().stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(themeResponses);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<ThemeResponse>> getWeeklyTopThemes() {
        List<ThemeResponse> themeResponses = themeService.findWeeklyTop10Themes().stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(themeResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent()
                .build();
    }
}
