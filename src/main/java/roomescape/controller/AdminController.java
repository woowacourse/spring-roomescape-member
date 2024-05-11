package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.config.AuthenticationPrincipal;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.dto.AdminReservationRequest;
import roomescape.service.ReservationService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String admin() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String time() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String theme() {
        return "admin/theme";
    }

    @PostMapping("/reservations")
    @ResponseBody
    public ResponseEntity<Reservation> saveAdminReservation(@AuthenticationPrincipal Member member, @RequestBody AdminReservationRequest adminReservationRequest) {
        Reservation savedReservation = reservationService.save(member, adminReservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + savedReservation.getId())).body(savedReservation);
    }

    @GetMapping(path = "/reservations", params = {"memberId", "themeId", "dateFrom", "dateTo"})
    public ResponseEntity<List<Reservation>> findFilteredReservation(
            @RequestParam(name = "memberId") Long memberId,
            @RequestParam(name = "themeId") Long themeId,
            @RequestParam(name = "dateFrom") LocalDate dateFrom,
            @RequestParam(name = "dateTo") LocalDate dateTo) {
        List<Reservation> reservations = reservationService.findFilteredReservation(memberId, themeId, dateFrom, dateTo);

        return ResponseEntity.ok(reservations);
    }
}
