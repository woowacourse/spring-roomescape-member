package roomescape.theme;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping()
    public ResponseEntity<?> addTheme(@RequestBody ThemeRequest themeRequest) {
        Long id = themeService.save(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
}
