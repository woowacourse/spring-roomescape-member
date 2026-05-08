package roomescape.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.domain.Theme;
import roomescape.dto.response.ThemeResponse;
import roomescape.service.ThemeService;

import java.util.List;

@Controller
public class UserViewController {

    private final ThemeService themeService;

    public UserViewController(ThemeService themeService) {
        this.themeService = themeService;
    }

    private static final int CAROUSEL_FALLBACK_SIZE = 5;

    @GetMapping("/")
    public String home(Model model) {
        List<Theme> popular = themeService.findPopularThemes();
        List<Theme> allThemes = themeService.getThemes();
        if (popular.isEmpty()) {
            popular = allThemes.stream().limit(CAROUSEL_FALLBACK_SIZE).toList();
        }
        model.addAttribute("popularThemes", ThemeResponse.from(popular));
        model.addAttribute("themes", ThemeResponse.from(allThemes));
        return "index";
    }

    @GetMapping("/popular")
    public String popular(Model model) {
        model.addAttribute("popularThemes", ThemeResponse.from(themeService.findPopularThemes()));
        return "popular";
    }

    @GetMapping("/reservation")
    public String reservation(@RequestParam(value = "themeId", required = false) Long themeId, Model model) {
        if (themeId != null) {
            model.addAttribute("selectedTheme", ThemeResponse.from(themeService.findById(themeId)));
        }
        return "reservation";
    }

    @GetMapping("/reservation/success")
    public String reservationSuccess() {
        return "reservation-success";
    }

    @GetMapping("/reservation-mine")
    public String reservationMine() {
        return "reservation-mine";
    }
}
