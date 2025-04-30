package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;
import roomescape.service.ThemeService;

@Controller
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/admin/theme")
    public String displayAdminTheme() {
        return "/admin/theme";
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponseDto> createTheme(
            @RequestBody ThemeRequestDto themeRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(themeService.createTheme(themeRequest));
    }
}
