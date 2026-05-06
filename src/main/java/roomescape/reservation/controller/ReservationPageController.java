package roomescape.reservation.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.reservation.service.ReservationService;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.service.ThemeService;

@Controller
@RequestMapping("/pages/user/reservations")
public class ReservationPageController {

    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationPageController(
            final ReservationService reservationService, ReservationTimeService reservationTimeService,
            final ThemeService themeService
    ) {
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    @GetMapping
    public String read(
            @RequestParam(required = false) final Long themeId,
            @RequestParam(required = false) final LocalDate date,
            @RequestParam(defaultValue = "7") final int period,
            @RequestParam(defaultValue = "10") final int limit,
            final Model model
    ) {
        model.addAttribute("reservations", reservationService.getAll());
        model.addAttribute("themes", themeService.getAll());
        model.addAttribute("popularThemes", themeService.getPopularThemes(period, limit));
        model.addAttribute("selectedThemeId", themeId);
        model.addAttribute("selectedDate", date);
        model.addAttribute("period", period);
        model.addAttribute("limit", limit);

        List<ReservationTime> availableTimes = List.of();
        if (themeId != null && date != null) {
            availableTimes = reservationTimeService.findAvailableTimes(date, themeId);
        }
        model.addAttribute("availableTimes", availableTimes);

        return "reservation/list";
    }

    @PostMapping
    public String create(
            @RequestParam final String name,
            @RequestParam final LocalDate date,
            @RequestParam final Long timeId
    ) {
        reservationService.save(name, date, timeId);
        return "redirect:/pages/user/reservations";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable final Long id) {
        reservationService.deleteById(id);
        return "redirect:/pages/user/reservations";
    }
}
