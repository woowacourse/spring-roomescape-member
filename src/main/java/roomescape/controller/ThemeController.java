package roomescape.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public List<ThemeResponse> read() {
        return themeService.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @PostMapping("/themes")
    public ThemeResponse create(@RequestBody ThemeRequest themeRequest) {
        Theme theme = themeRequest.toEntity();
        Theme savedTheme = themeService.save(theme);
        return ThemeResponse.from(savedTheme);
    }

    @DeleteMapping("/themes{id}")
    public void delete(@PathVariable Long id) {
        themeService.deleteById(id);
    }
}
