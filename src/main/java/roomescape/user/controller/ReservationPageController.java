package roomescape.user.controller;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import roomescape.theme.ThemeService;
import roomescape.user.service.ReservationService;
import roomescape.user.service.ReservationTimeService;

@Controller
public class ReservationPageController {

    private final ThemeService themeService;
    private final ReservationTimeService timeService;
    private final ReservationService reservationService;

    public ReservationPageController(
            ThemeService themeService,
            ReservationTimeService timeService,
            ReservationService reservationService
    ) {
        this.themeService = themeService;
        this.timeService = timeService;
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public String reservationPage(Model model) {
        model.addAttribute("themes", themeService.findAll());
        model.addAttribute("times", timeService.findAll());
        model.addAttribute("reservations", reservationService.findAll());
        model.addAttribute("today", LocalDate.now());
        return "index";
    }

    @PostMapping("/reservations")
    public String createReservation(
            @RequestParam String name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long timeId,
            @RequestParam Long themeId,
            RedirectAttributes redirectAttributes
    ) {
        reservationService.add(name, date, timeId, themeId);
        redirectAttributes.addFlashAttribute("message", "예약이 등록되었습니다.");
        return "redirect:/";
    }

    @PostMapping("/reservations/{id}/delete")
    public String deleteReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reservationService.delete(id);
        redirectAttributes.addFlashAttribute("message", "예약이 취소되었습니다.");
        return "redirect:/";
    }
}
