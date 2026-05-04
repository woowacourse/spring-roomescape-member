package roomescape.theme;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(@Valid @RequestBody ThemeRequest themeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(themeService.create(themeRequest));
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> read() {
        return ResponseEntity.status(HttpStatus.OK).body(themeService.read());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/popularity")
    public ResponseEntity<List<ThemeResponse>> readByPopularity() {
        return ResponseEntity.status(HttpStatus.OK).body(themeService.readPopularThemes());
    }
}
