package roomescape.controller.theme;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import roomescape.controller.reservation.dto.PopularThemeResponse;
import roomescape.controller.theme.dto.CreateThemeRequest;
import roomescape.controller.theme.dto.ThemeResponse;
import roomescape.service.ThemeService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<ThemeResponse> getThemes() {
        return themeService.getThemes();
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> addTheme(@RequestBody
                                                  @Validated final CreateThemeRequest createThemeRequest) {
        final ThemeResponse theme = themeService.addTheme(createThemeRequest);
        final URI uri = UriComponentsBuilder.fromPath("/themes/{id}")
                .buildAndExpand(theme.id())
                .toUri();

        return ResponseEntity.created(uri)
                .body(theme);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable("id") final Long id) {
        final int deleteCount = themeService.deleteTheme(id);
        if (deleteCount == 0) {
            return ResponseEntity.notFound()
                    .build();
        }
        return ResponseEntity.noContent()
                .build();
    }

    //TODO rest 한가?
    @GetMapping("/popular")
    public List<PopularThemeResponse> getPopularThemes() {
        return themeService.getPopularThemes(LocalDate.now());
    }
}
