package roomescape.theme.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.theme.entity.Theme;
import roomescape.theme.payload.ThemeRequest;
import roomescape.theme.payload.ThemeResponse;
import roomescape.theme.service.ThemeService;

@Controller
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> postTheme(@Valid @RequestBody ThemeRequest request) {
        Theme theme = themeService.save(request);
        return ResponseEntity.ok().body(ThemeResponse.from(theme));
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> getAllTimes() {
        List<ThemeResponse> responses = themeService.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();

        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTimes(@PathVariable Long id) {
        themeService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
