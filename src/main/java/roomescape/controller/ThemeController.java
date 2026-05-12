package roomescape.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;
import roomescape.service.dto.request.ThemeCreateRequest;
import roomescape.service.dto.response.ThemeResponse;

import java.util.List;

@RestController
@RequestMapping("/themes")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;
    private final ReservationTimeService reservationTimeService;

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        final List<ThemeResponse> results = themeService.getThemes();
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ThemeResponse>> getPopularThemes() {
        final List<ThemeResponse> results = themeService.getPopularThemes();
        return ResponseEntity.ok().body(results);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(
            @RequestBody ThemeCreateRequest request
    ) {
        final ThemeResponse result = themeService.create(request);
        return ResponseEntity.created(URI.create("/themes/" + result.id()))
                .body(result);
    }

    @DeleteMapping("/{theme-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("theme-id") Long themeId
    ) {
        themeService.delete(themeId);
        return ResponseEntity.noContent().build();
    }
}
