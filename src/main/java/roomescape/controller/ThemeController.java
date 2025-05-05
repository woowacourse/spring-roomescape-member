package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ThemeRequestDto;
import roomescape.model.Theme;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<Theme> addTheme(@RequestBody ThemeRequestDto themeRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(themeService.addTheme(themeRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<Theme>> getAllThemes() {
        return ResponseEntity.status(HttpStatus.OK).body(themeService.getAllThemes());
    }

    @GetMapping("/best/weekly")
    public ResponseEntity<List<Theme>> getBestWeeklyThemes() {
        return ResponseEntity.ok(themeService.getWeeklyBestThemes());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
