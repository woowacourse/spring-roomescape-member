package roomescape.admin;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.ThemeService;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;

@RestController
@RequestMapping("/admin/themes")
public class AdminThemeController {

    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(@Valid @RequestBody ThemeRequest themeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(themeService.create(themeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
