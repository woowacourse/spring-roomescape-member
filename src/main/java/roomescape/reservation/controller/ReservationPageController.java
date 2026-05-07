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
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.reservationtime.controller.dto.ReservationTimeResponse;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

@Controller
@RequestMapping("/pages/user/reservations")
public class ReservationPageController {

    private final ReservationService reservationService;
    private final ThemeService themeService;

    public ReservationPageController(
            final ReservationService reservationService,
            final ThemeService themeService
    ) {
        this.reservationService = reservationService;
        this.themeService = themeService;
    }

    @GetMapping
    public String getReservationPage(
            @RequestParam(required = false) final Long themeId,
            @RequestParam(required = false) final LocalDate date,
            @RequestParam(defaultValue = "7") final int period,
            @RequestParam(defaultValue = "10") final int limit,
            final Model model
    ) {
        model.addAttribute("reservations", reservationService.getAll().stream()
                .map(ReservationResponse::from)
                .toList());
        model.addAttribute("themes", themeService.getAll().stream()
                .map(ThemeResponse::from)
                .toList());
        model.addAttribute("popularThemes", reservationService.getPopularThemes(period, limit).stream()
                .map(ThemeResponse::from)
                .toList());
        model.addAttribute("selectedThemeId", themeId);
        model.addAttribute("selectedDate", date);
        model.addAttribute("period", period);
        model.addAttribute("limit", limit);

        List<ReservationTimeResponse> availableTimes = List.of();
        if (themeId != null && date != null) {
            availableTimes = reservationService.findAvailableTimes(date, themeId).stream()
                    .map(ReservationTimeResponse::from)
                    .toList();
        }
        model.addAttribute("availableTimes", availableTimes);

        return "reservation/list";
    }

    @PostMapping
    public String createReservation(
            @RequestParam final String name,
            @RequestParam final LocalDate date,
            @RequestParam final Long timeId
    ) {
        reservationService.save(name, date, timeId);
        return "redirect:/pages/user/reservations";
    }

    @PostMapping("/{id}/delete")
    public String deleteReservation(@PathVariable final Long id) {
        reservationService.deleteById(id);
        return "redirect:/pages/user/reservations";
    }
}
