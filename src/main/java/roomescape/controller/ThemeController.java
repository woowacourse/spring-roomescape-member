package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.theme.ThemeCreateRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> readAll() {
        return ResponseEntity.ok(themeService.findAll());
    }

    @GetMapping("/populars")
    public ResponseEntity<List<ThemeResponse>> readPopularThemes() {
        LocalDate today = LocalDate.now(KST_ZONE);
        LocalDate from = today.minusWeeks(1);
        LocalDate to = today.minusDays(1);
        return ResponseEntity.ok(themeService.findPopulars(from, to));
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(@RequestBody ThemeCreateRequest request) {
        ThemeResponse result = themeService.add(request);
        return ResponseEntity.created(URI.create("/themes/" + result.getId()))
                .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
