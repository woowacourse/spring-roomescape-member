package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.OK)
    public List<ThemeResponse> findAll() {
        return themeService.findAll().stream()
                .map(ThemeResponse::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ThemeResponse find(@PathVariable Long id) {
        return ThemeResponse.toDto(themeService.find(id));
    }

    @GetMapping("/famous")
    @ResponseStatus(HttpStatus.OK)
    public List<ThemeResponse> findFamous(@ModelAttribute ThemeFamousFindRequest request) {
        return themeService.findFamous(request).stream()
                .map(ThemeResponse::toDto)
                .toList();
    }
}
