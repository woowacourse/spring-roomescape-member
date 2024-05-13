package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeCreateRequest;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/themes")
public class ThemeRestController {

    private final ThemeService themeService;

    public ThemeRestController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<ThemeResponse> responses = themeService.findAll();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<ThemeResponse>> getRankThemes() {
        List<ThemeResponse> responses = themeService.findRank();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<Long> createTheme(@Valid @RequestBody ThemeCreateRequest request) {
        long id= themeService.save(request);

        URI location = URI.create("/themes/" + id);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable(name = "id") Long id) {
        themeService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
