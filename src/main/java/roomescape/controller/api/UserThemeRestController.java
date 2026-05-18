package roomescape.controller.api;

import org.springframework.web.bind.annotation.*;
import roomescape.common.dto.ApiResponse;
import roomescape.domain.theme.dto.ThemeResponse;
import roomescape.service.ThemeService;

import java.util.List;

@RequestMapping("/themes")
@RestController
public class UserThemeRestController {

    private final ThemeService themeService;

    public UserThemeRestController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ApiResponse<List<ThemeResponse>> readAll() {
        return new ApiResponse<>(themeService.findAll());
    }

    @GetMapping("/popular")
    public ApiResponse<List<ThemeResponse>> readPopularTheme(
            @RequestParam(defaultValue = "7") Integer period,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        return new ApiResponse<>(themeService.findPopularTheme(period, limit));
    }
}

