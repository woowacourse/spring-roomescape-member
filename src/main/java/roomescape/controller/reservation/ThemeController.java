package roomescape.controller.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.ThemeAddRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> addTime(
            @RequestBody ThemeAddRequest themeAddRequest) {
        ThemeResponse themeResponse = themeService.addTheme(themeAddRequest);
        return ResponseEntity.created(URI.create("/times/" + themeResponse.id()))
                .body(themeResponse);
    }

    @GetMapping
    public List<ThemeResponse> findTimes() {
        return themeService.findThemes();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
