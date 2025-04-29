package roomescape.reservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.controller.dto.ThemeRequest;
import roomescape.reservation.controller.dto.ThemeResponse;
import roomescape.reservation.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ThemeResponse addTheme(@RequestBody ThemeRequest request) {
        return themeService.add(request);
    }
}
