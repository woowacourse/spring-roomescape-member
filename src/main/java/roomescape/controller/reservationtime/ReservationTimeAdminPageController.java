package roomescape.controller.reservationtime;

import java.time.LocalTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.controller.reservationtime.dto.ReservationTimeResponse;
import roomescape.service.reservationtime.ReservationTimeService;

@Controller
@RequestMapping("/pages/admin/reservation-times")
public class ReservationTimeAdminPageController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeAdminPageController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public String getReservationTimeAdminPage(final Model model) {
        model.addAttribute("reservationTimes", reservationTimeService.getAll().stream()
                .map(ReservationTimeResponse::from)
                .toList());
        return "reservationtime/list";
    }

    @PostMapping
    public String createReservationTime(@RequestParam final String startAt) {
        reservationTimeService.save(LocalTime.parse(startAt));
        return "redirect:/pages/admin/reservation-times";
    }

    @PostMapping("/{timeId}/delete")
    public String deleteReservationTime(@org.springframework.web.bind.annotation.PathVariable final Long timeId) {
        reservationTimeService.deleteById(timeId);
        return "redirect:/pages/admin/reservation-times";
    }
}
