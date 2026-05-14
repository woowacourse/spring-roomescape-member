package roomescape.theme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import roomescape.theme.service.ThemeService;

@Controller
public class AdminThemePageController {

    private final ThemeService themeService;

    public AdminThemePageController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/admin")
    public String adminThemePage(Model model) {
        model.addAttribute("themes", themeService.findAll());
        return "admin";
    }

    @GetMapping("/admin.html")
    public String redirectLegacyAdminPage() {
        return "redirect:/admin";
    }

    @PostMapping("/admin/theme")
    public String createTheme(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String image,
            RedirectAttributes redirectAttributes
    ) {
        themeService.addTheme(name, description, image);
        redirectAttributes.addFlashAttribute("message", "테마가 등록되었습니다.");
        return "redirect:/admin";
    }

    @PostMapping("/admin/theme/{id}/deleteById")
    public String deleteTheme(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        themeService.removeById(id);
        redirectAttributes.addFlashAttribute("message", "테마가 삭제되었습니다.");
        return "redirect:/admin";
    }
}
