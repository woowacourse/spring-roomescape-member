package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.service.dto.ReservationTimeResponse;
import roomescape.service.dto.ThemeResponse;
import roomescape.service.theme.ThemeFindService;

import java.util.List;

@RestController
public class ThemeController {

    private final ThemeFindService themeFindService;

    public ThemeController(ThemeFindService themeFindService) {
        this.themeFindService = themeFindService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> getReservationTimes() {
        List<Theme> themes = themeFindService.findThemes();
        return ResponseEntity.ok(ThemeResponse.listOf(themes));
    }
}
