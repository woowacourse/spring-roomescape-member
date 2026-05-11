package roomescape.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ThemeAllResponse;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> register(@RequestBody ThemeRequest themeRequest) {
        ThemeResponse themeResponse = themeService.register(themeRequest);
        return ResponseEntity.created(URI.create("/themes/" + themeResponse.id()))
                .body(themeResponse);
    }

    @GetMapping
    public ResponseEntity<ThemeAllResponse> readAll() {
        ThemeAllResponse themeResponses = themeService.readAll();
        return ResponseEntity.ok().body(themeResponses);
    }

    @GetMapping("/ranks")
    public ResponseEntity<ThemeAllResponse> readRanks(@RequestParam("limit") Long limit) {
        if (limit == null || limit <= 0 || limit > 30) {
            return ResponseEntity.badRequest().build();
        }
        ThemeAllResponse themeResponses = themeService.readRanks(limit);
        return ResponseEntity.ok().body(themeResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeById(@PathVariable Long id) {
        themeService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
