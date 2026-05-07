package roomescape.reservationtime.controller;

import java.time.LocalTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.reservationtime.controller.dto.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

@Controller
@RequestMapping("/pages/admin/themes/{themeId}/times")
public class ReservationTimeAdminPageController {

    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationTimeAdminPageController(
            final ReservationTimeService reservationTimeService,
            final ThemeService themeService
    ) {
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    @GetMapping
    public String getReservationTimeAdminPage(@PathVariable final Long themeId, final Model model) {
        model.addAttribute("theme", ThemeResponse.from(themeService.getById(themeId)));
        model.addAttribute("themes", themeService.getAll().stream()
                .map(ThemeResponse::from)
                .toList());
        model.addAttribute("reservationTimes", reservationTimeService.findAllByThemeId(themeId).stream()
                .map(ReservationTimeResponse::from)
                .toList());
        return "reservationtime/list";
    }

    @PostMapping
    public String createReservationTime(
            @PathVariable final Long themeId,
            @RequestParam final String startAt
    ) {
        reservationTimeService.save(LocalTime.parse(startAt), themeId);
        return "redirect:/pages/admin/themes/" + themeId + "/times";
    }

    @PostMapping("/{timeId}/delete")
    public String deleteReservationTime(
            @PathVariable final Long themeId,
            @PathVariable final Long timeId
    ) {
        reservationTimeService.deleteById(timeId);
        return "redirect:/pages/admin/themes/" + themeId + "/times";
    }
}
