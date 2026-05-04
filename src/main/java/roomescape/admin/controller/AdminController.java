package roomescape.admin.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.dto.RequestTheme;
import roomescape.theme.dto.ResponseTheme;
import roomescape.theme.service.ThemeService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ThemeService themeService;

    public AdminController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/themes")
    public ResponseTheme createTheme(@RequestBody RequestTheme requestTheme) {
        return ResponseTheme.from(themeService.createTheme(requestTheme.name(), requestTheme.description(), requestTheme.thumbnail()));
    }
}
