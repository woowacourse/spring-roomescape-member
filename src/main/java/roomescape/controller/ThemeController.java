package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme.ThemeCommand;
import roomescape.dto.theme.AddThemeRequest;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping()
    public void getThemes() {
        /*service*/
    }

    @PostMapping()
    public void addTheme(@RequestBody @Valid AddThemeRequest addThemeRequest) {
        ThemeCommand themeCommand = addThemeRequest.from();
        themeService.addTheme(themeCommand);
    }

    @DeleteMapping("/{id}")
    public void deleteTheme(@PathVariable long id) {
        /*service.deletebyID(id)*/
    }
}
