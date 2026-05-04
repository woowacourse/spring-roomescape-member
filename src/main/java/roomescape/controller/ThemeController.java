package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ThemeCreateRequest;
import roomescape.controller.dto.ThemeResponse;
import roomescape.controller.mapper.ThemeMapper;
import roomescape.domain.Theme;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;
    private final ThemeMapper themeMapper;

    public ThemeController(ThemeService themeService, ThemeMapper themeMapper) {
        this.themeService = themeService;
        this.themeMapper = themeMapper;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(
            ThemeCreateRequest themeCreateRequest
    ) {
        Theme theme = themeService.create(themeCreateRequest);
        ThemeResponse response = themeMapper.mapToResponse(theme);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
