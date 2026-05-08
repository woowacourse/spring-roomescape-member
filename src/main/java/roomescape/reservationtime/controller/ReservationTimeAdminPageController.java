package roomescape.reservationtime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.service.ThemeService;

@Controller
@RequestMapping("/pages/admin/themes/{themeId}/times")
@RequiredArgsConstructor
public class ReservationTimeAdminPageController {

    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    @GetMapping
    public String read(@PathVariable final Long themeId, final Model model) {
        model.addAttribute("theme", themeService.getById(themeId));
        model.addAttribute("themes", themeService.getAll());
        model.addAttribute("reservationTimes", reservationTimeService.findAllByThemeId(themeId));
        return "reservationtime/list";
    }

    @PostMapping
    public String create(
            @PathVariable final Long themeId,
            @RequestParam final String startAt
    ) {
        reservationTimeService.save(java.time.LocalTime.parse(startAt), themeId);
        return "redirect:/pages/admin/themes/" + themeId + "/times";
    }

    @PostMapping("/{timeId}/delete")
    public String delete(
            @PathVariable final Long themeId,
            @PathVariable final Long timeId
    ) {
        reservationTimeService.deleteById(timeId);
        return "redirect:/pages/admin/themes/" + themeId + "/times";
    }
}
