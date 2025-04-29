package roomescape.reservation.presentation;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.reservation.application.service.ThemeService;
import roomescape.reservation.presentation.dto.ThemeRequest;
import roomescape.reservation.presentation.dto.ThemeResponse;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> createTheme(
            final @RequestBody ThemeRequest request
    ) {
        ThemeResponse theme = themeService.createTheme(request);

        return ResponseEntity.created(createUri(theme.getId())).body(theme);
    }

    private URI createUri(Long themeId){
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(themeId)
                .toUri();
    }
}
