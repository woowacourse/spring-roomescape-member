package roomescape.theme.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.controller.dto.CreateThemeRequest;
import roomescape.theme.controller.dto.PopularThemeResponse;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.theme.service.dto.CreateThemeServiceRequest;

@RequestMapping("/themes")
@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ThemeResponse addTheme(@RequestBody CreateThemeRequest request) {
        final CreateThemeServiceRequest serviceRequest = request.toCreateThemeServiceRequest();
        final Theme savedTheme = themeService.addTheme(serviceRequest);
        return ThemeResponse.from(savedTheme);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ThemeResponse> findAllThemes() {
        return themeService.findAllThemes()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<PopularThemeResponse> findPopularThemes() {
        return themeService.findPopularThemes()
                .stream()
                .map(PopularThemeResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheme(@PathVariable long id) {
        themeService.deleteTheme(id);
    }
}
