package roomescape.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ThemeFamousFindRequest;
import roomescape.controller.dto.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public List<ThemeResponse> findAll() {
        return themeService.findAll().stream()
                .map(ThemeResponse::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ThemeResponse find(@PathVariable Long id) {
        return ThemeResponse.toDto(themeService.find(id));
    }

    @GetMapping("/famous")
    public List<ThemeResponse> findFamous(@ModelAttribute ThemeFamousFindRequest request) {
        return themeService.findFamous(request).stream()
                .map(ThemeResponse::toDto)
                .toList();
    }
}
