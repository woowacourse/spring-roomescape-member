package roomescape.controller.api;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.CreateThemeRequest;
import roomescape.controller.dto.CreateThemeResponse;
import roomescape.domain.theme.Theme;
import roomescape.service.ThemeService;
import roomescape.service.dto.SaveThemeDto;

@RestController
@RequestMapping("/admin/themes")
public class AdminThemeController {

    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<CreateThemeResponse> save(@RequestBody CreateThemeRequest request) {
        Theme newTheme = themeService.save(
            new SaveThemeDto(request.name(), request.description(), request.thumbnail()));
        Long id = newTheme.getId();

        return ResponseEntity.created(URI.create("/themes/" + id))
            .body(new CreateThemeResponse(
                id,
                newTheme.getName(),
                newTheme.getDescription(),
                newTheme.getThumbnail()
            ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
