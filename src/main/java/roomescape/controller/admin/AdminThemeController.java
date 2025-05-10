package roomescape.controller.admin;

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
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeCommandService;
import roomescape.service.ThemeQueryService;

@RestController
@RequestMapping("/admin/themes")
public class AdminThemeController {

    private final ThemeCommandService themeCommandService;
    private final ThemeQueryService themeQueryService;

    public AdminThemeController(ThemeCommandService themeCommandService, ThemeQueryService themeQueryService) {
        this.themeCommandService = themeCommandService;
        this.themeQueryService = themeQueryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ThemeResponse> readThemes() {
        return themeQueryService.findAllThemes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ThemeResponse createTheme(
            @RequestBody ThemeRequest request
    ) {
        return themeCommandService.createTheme(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTheme(
            @PathVariable Long id
    ) {
        themeCommandService.deleteTheme(id);
    }
}
