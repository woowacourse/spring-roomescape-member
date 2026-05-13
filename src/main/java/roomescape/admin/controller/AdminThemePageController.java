package roomescape.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import roomescape.admin.service.AdminThemeService;

@Controller
public class AdminThemePageController {

    private final AdminThemeService adminThemeService;

    public AdminThemePageController(AdminThemeService adminThemeService) {
        this.adminThemeService = adminThemeService;
    }

    @GetMapping("/admin")
    public String adminThemePage(Model model) {
        model.addAttribute("themes", adminThemeService.findAll());
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
        adminThemeService.addTheme(name, description, image);
        redirectAttributes.addFlashAttribute("message", "테마가 등록되었습니다.");
        return "redirect:/admin";
    }

    @PostMapping("/admin/theme/{id}/deleteById")
    public String deleteTheme(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminThemeService.removeById(id);
        redirectAttributes.addFlashAttribute("message", "테마가 삭제되었습니다.");
        return "redirect:/admin";
    }
}
