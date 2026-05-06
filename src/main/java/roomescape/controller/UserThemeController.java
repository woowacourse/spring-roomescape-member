package roomescape.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ThemeResponse;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/user/themes")
public class UserThemeController {

    private final ThemeService themeService;
    private final ReservationTimeService reservationTimeService;

    public UserThemeController(
            ThemeService themeService,
            ReservationTimeService reservationTimeService
    ) {
        this.themeService = themeService;
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public List<ThemeResponse> list() {
        return themeService.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
