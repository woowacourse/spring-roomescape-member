package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ThemeCreateRequest;
import roomescape.controller.dto.ThemeFamousFindRequest;
import roomescape.domain.Theme;
import roomescape.service.ThemeService;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @DeleteMapping({"/admin/themes/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        themeService.delete(id);
    }

    @PostMapping("/admin/themes")
    @ResponseStatus(HttpStatus.CREATED)
    public Theme create(@RequestBody ThemeCreateRequest request) {
        return themeService.create(request);
    }

    @GetMapping("/themes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Theme find(@PathVariable Long id) {
        return themeService.find(id);
    }

    @GetMapping("/themes")
    @ResponseStatus(HttpStatus.OK)
    public List<Theme> findAll() {
        return themeService.findAll();
    }

    @GetMapping("/themes/famous")
    @ResponseStatus(HttpStatus.OK)
    public List<Theme> findFamous(@ModelAttribute ThemeFamousFindRequest request) {
        return themeService.findFamous(request);
    }
}
