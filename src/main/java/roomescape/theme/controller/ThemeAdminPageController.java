package roomescape.theme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.theme.service.ThemeService;

@Controller
@RequestMapping("/pages/admin/themes")
public class ThemeAdminPageController {

    private final ThemeService themeService;

    public ThemeAdminPageController(final ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public String read(final Model model) {
        model.addAttribute("themes", themeService.getAll());
        return "theme/list";
    }

    @PostMapping
    public String create(
            @RequestParam final String name,
            @RequestParam final String description,
            @RequestParam final String thumbnailUrl
    ) {
        themeService.save(name, description, thumbnailUrl);
        return "redirect:/pages/admin/themes";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable final Long id) {
        themeService.deleteById(id);
        return "redirect:/pages/admin/themes";
    }
}
