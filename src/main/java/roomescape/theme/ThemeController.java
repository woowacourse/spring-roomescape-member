package roomescape.theme;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.dto.ThemeResponse;

@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> read() {
        return ResponseEntity.status(HttpStatus.OK).body(themeService.read());
    }

    @GetMapping("/popularity")
    public ResponseEntity<List<ThemeResponse>> readByPopularity() {
        LocalDate now = LocalDate.now();
        return ResponseEntity.status(HttpStatus.OK).body(themeService.readPopularThemes(now));
    }
}
