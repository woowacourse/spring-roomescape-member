package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> findAll() {
        List<ThemeResponse> timeSlots = themeService.findAll();
        return ResponseEntity.ok(timeSlots);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(@RequestBody final ThemeRequest themeRequest) {
        ThemeResponse themeResponse = themeService.create(themeRequest);
        return ResponseEntity.created(URI.create("/themes/" + themeResponse.id())).body(themeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
