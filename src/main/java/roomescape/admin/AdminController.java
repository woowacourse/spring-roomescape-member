package roomescape.admin;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.reservation.dto.ReservationAdminRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.ReservationTimeService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;

    public AdminController(ReservationService reservationService, ReservationTimeService reservationTimeService) {
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String readReservation(Model model) {
        model.addAttribute("reservations", reservationService.findAllReservations());
        return "admin/reservation-new";
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> reservationByFilter(
            @RequestParam(value = "themeId", required = false) Long themeId,
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) LocalDate dateTo) {
        return ResponseEntity.ok(reservationService.findReservationsBy(themeId, memberId, dateFrom, dateTo));
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> postReservations(
            @RequestBody @Valid ReservationAdminRequest reservationAdminRequest) {
        ReservationResponse response = reservationService.createByAdmin(reservationAdminRequest);
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @GetMapping("/time")
    public String time(Model model) {
        model.addAttribute("reservationTimes", reservationTimeService.findAll());
        return "admin/time";
    }

    @GetMapping("/theme")
    public String theme() {
        return "admin/theme";
    }
}
