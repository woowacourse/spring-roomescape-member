package roomescape.theme;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/themes")
public class AdminThemeController {

    private final AdminThemeService adminThemeService;

    public AdminThemeController(AdminThemeService adminThemeService) {
        this.adminThemeService = adminThemeService;
    }

    @PostMapping()
    public ResponseEntity<?> addTheme(@RequestBody ThemeRequest themeRequest) {
        Long id = adminThemeService.save(themeRequest.name(), themeRequest.description(), themeRequest.thumbnail());
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTheme(@PathVariable long id) {
        adminThemeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
