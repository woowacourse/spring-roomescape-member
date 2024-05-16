package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.reservation.dto.ThemeRequest;
import roomescape.reservation.dto.ThemeResponse;
import roomescape.reservation.service.ThemeService;
import roomescape.util.AdminRequired;

@Controller
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> findAll() {
        return ResponseEntity.ok(themeService.findAllThemes());
    }

    @PostMapping
    @AdminRequired
    public ResponseEntity<ThemeResponse> create(@RequestBody @Valid ThemeRequest themeRequest) {
        ThemeResponse response = themeService.create(themeRequest);
        return ResponseEntity.created(URI.create("/themes/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    @AdminRequired
    public ResponseEntity<Void> delete(@PathVariable("id") long themeId) {
        themeService.delete(themeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> findPopular() {
        return ResponseEntity.ok(themeService.findPopularThemes());
    }
}
