package roomescape.reservation.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.PopularThemeResponse;
import roomescape.reservation.dto.ThemeResponse;
import roomescape.reservation.dto.ThemeSaveRequest;
import roomescape.reservation.service.ThemeService;

@RestController
public class ThemeApiController {

    private final ThemeService themeService;

    public ThemeApiController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes/popular")
    public ResponseEntity<List<PopularThemeResponse>> findTopTenThemesOfLastWeek() {
        List<PopularThemeResponse> popularThemeResponses = themeService.findTopTenThemesDescendingOfLastWeek();

        return ResponseEntity.ok(popularThemeResponses);
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> findAll() {
        List<ThemeResponse> themeResponses = themeService.findAll();

        return ResponseEntity.ok(themeResponses);
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> save(@RequestBody ThemeSaveRequest themeSaveRequest) {
        Long saveId = themeService.save(themeSaveRequest);
        ThemeResponse themeResponse = themeService.findById(saveId);

        return ResponseEntity.created(URI.create("/themes/" + saveId)).body(themeResponse);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
