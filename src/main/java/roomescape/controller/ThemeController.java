package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping
    public String displayThemeRank() {
        return "/index";
    }

    @GetMapping("/admin/theme")
    public String displayAdminTheme() {
        return "/admin/theme";
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponseDto>> readThemes() {
        List<ThemeResponseDto> themeResponseDtos = themeService.findAllThemes();
        return ResponseEntity.ok().body(themeResponseDtos);
    }

    @GetMapping("/themes/rank")
    public ResponseEntity<List<ThemeResponseDto>> readThemeRank() {
        List<ThemeResponseDto> themeResponseDtos = themeService.findThemeRank();
        return ResponseEntity.ok().body(themeResponseDtos);
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponseDto> createTheme(
            @RequestBody ThemeRequestDto themeRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(themeService.createTheme(themeRequest));
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(
            @PathVariable("id") Long idRequest
    ) {
        themeService.deleteTheme(idRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
