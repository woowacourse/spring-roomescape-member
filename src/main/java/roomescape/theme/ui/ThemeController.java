package roomescape.theme.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.application.ThemeService;
import roomescape.theme.ui.dto.ThemeResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ThemeController.BASE_PATH)
public class ThemeController {

    public static final String BASE_PATH = "/theme";

    public ThemeService themeService;

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAll() {
        return ResponseEntity.ok(themeService.getAll());
    }

    @PostMapping
    public ResponseEntity<Void> create() {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        return null;
    }
}
