package roomescape.web.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ThemeService;
import roomescape.service.dto.request.ThemeRequest;
import roomescape.service.dto.response.ThemeResponse;
import roomescape.web.api.dto.ThemeListResponse;

import java.net.URI;
import java.util.List;

@RestController
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> save(@RequestBody @Valid ThemeRequest request) {
        ThemeResponse themeResponse = themeService.save(request);

        return ResponseEntity.created(URI.create("/themes/" + themeResponse.id()))
                .body(themeResponse);
    }

    @GetMapping("/themes")
    public ResponseEntity<ThemeListResponse> findAll() {
        List<ThemeResponse> themeResponses = themeService.findAll();

        return ResponseEntity.ok(new ThemeListResponse(themeResponses));
    }

    @GetMapping("/themes/tops")
    public ResponseEntity<ThemeListResponse> findTops() {
        List<ThemeResponse> themeResponses = themeService.findTopThemes();

        return ResponseEntity.ok(new ThemeListResponse(themeResponses));
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        themeService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
