package roomescape.controller.reservation;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.security.Permission;
import roomescape.domain.member.Role;
import roomescape.domain.reservation.Theme;
import roomescape.service.dto.reservation.ThemeResponse;
import roomescape.service.dto.reservation.ThemeSaveRequest;
import roomescape.service.reservation.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
@Permission(role = Role.ADMIN)
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    @Permission(role = Role.GUEST)
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<ThemeResponse> themeResponses = themeService.findThemes().stream()
                .map(ThemeResponse::of)
                .toList();
        return ResponseEntity.ok(themeResponses);
    }

    @GetMapping("/ranks")
    @Permission(role = Role.GUEST)
    public ResponseEntity<List<ThemeResponse>> getThemeRanks() {
        List<ThemeResponse> themeResponses = themeService.findTop10Recent7Days().stream()
                .map(ThemeResponse::of)
                .toList();
        return ResponseEntity.ok(themeResponses);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> addTheme(@RequestBody @Valid ThemeSaveRequest request) {
        Theme theme = themeService.createTheme(request);
        return ResponseEntity.created(URI.create("/themes/" + theme.getId()))
                .body(ThemeResponse.of(theme));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
