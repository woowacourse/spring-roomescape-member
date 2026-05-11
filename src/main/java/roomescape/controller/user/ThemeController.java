package roomescape.controller.user;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> readAll() {
        List<ThemeResponse> themeResponses = themeService.readAll();
        return ResponseEntity.ok().body(themeResponses);
    }

    @GetMapping("/ranks")
    public ResponseEntity<List<ThemeResponse>> readRanks() {
        List<ThemeResponse> themeResponses = themeService.readRanks(LocalDate.now());
        return ResponseEntity.ok().body(themeResponses);
    }
}
