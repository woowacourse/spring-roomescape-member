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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.dto.ThemeRequestDto;
import roomescape.model.Theme;
import roomescape.service.ThemeService;

@Controller
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

    @GetMapping("/themes")
    public ResponseEntity<List<Theme>> getThemes(
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "period", required = false) String period
    ) {
        if ("best".equalsIgnoreCase(sort) && "weekly".equalsIgnoreCase(period)) {
            return ResponseEntity.ok(themeService.getWeeklyBestThemes());
        }
        return ResponseEntity.badRequest().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
