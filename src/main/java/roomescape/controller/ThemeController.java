package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.controller.request.CreateThemeRequest;
import roomescape.controller.response.ThemeResponse;
import roomescape.service.ThemeService;

@Controller
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService service;

    public ThemeController(final ThemeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> register(@RequestBody @Valid CreateThemeRequest request) {
        var theme = service.register(request.name(), request.description(), request.thumbnail());
        var response = ThemeResponse.from(theme);
        return ResponseEntity.created(URI.create("/themes/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAllThemes() {
        var themes = service.findAllThemes();
        var response = ThemeResponse.from(themes);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/popular", params = {"startDate", "endDate", "count"})
    public ResponseEntity<List<ThemeResponse>> getAvailableTimes(
        @RequestParam("startDate") LocalDate startDate,
        @RequestParam("endDate") LocalDate endDate,
        @RequestParam("count") Integer count
    ) {
        var themes = service.findPopularThemes(startDate, endDate, count);
        var response = ThemeResponse.from(themes);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean isRemoved = service.removeById(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
