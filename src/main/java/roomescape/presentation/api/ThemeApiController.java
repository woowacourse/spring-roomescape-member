package roomescape.presentation.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthRequired;
import roomescape.auth.Role;
import roomescape.business.model.entity.Theme;
import roomescape.business.model.vo.UserRole;
import roomescape.business.service.ThemeService;
import roomescape.presentation.dto.request.ThemeCreateRequest;
import roomescape.presentation.dto.response.ThemeResponse;

import java.net.URI;
import java.util.List;

@RestController
public class ThemeApiController {

    private final ThemeService themeService;

    public ThemeApiController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/themes")
    @AuthRequired
    @Role(UserRole.ADMIN)
    public ResponseEntity<ThemeResponse> add(@RequestBody @Valid ThemeCreateRequest request) {
        Theme theme = themeService.addAndGet(request.name(), request.description(), request.thumbnail());
        return ResponseEntity.created(URI.create("/themes")).body(ThemeResponse.from(theme));
    }

    @GetMapping("/themes")
    @AuthRequired
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<Theme> themes = themeService.getAll();
        List<ThemeResponse> responses = ThemeResponse.from(themes);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/themes/popular")
    @AuthRequired
    public ResponseEntity<List<ThemeResponse>> getPopularThemes(@RequestParam(value = "size", defaultValue = "10") int size) {
        List<Theme> popularThemes = themeService.getPopular(size);
        List<ThemeResponse> responses = ThemeResponse.from(popularThemes);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/themes/{id}")
    @AuthRequired
    @Role(UserRole.ADMIN)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
