package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import roomescape.common.dto.ApiResponse;
import roomescape.domain.theme.dto.ThemeCreateRequest;
import roomescape.domain.theme.dto.ThemeResponse;
import roomescape.service.ThemeService;

import java.util.List;

@RequestMapping("/admin/themes")
@RestController
public class AdminThemeRestController {

    private final ThemeService themeService;

    public AdminThemeRestController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ApiResponse<ThemeResponse> create(@Valid @RequestBody ThemeCreateRequest themeRequest) {
        return new ApiResponse<>(themeService.create(themeRequest));
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

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        themeService.delete(id);
        return new ApiResponse<>(null);
    }
}

