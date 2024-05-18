package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.dto.request.ReservationAdminCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@Controller
@RequestMapping("admin")
public class AdminPageController {

    private final ReservationService reservationService;

    public AdminPageController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String showAdminIndexPage() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String showReservationPage() {
        return "admin/reservation-new";
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createAdminReservation(
            @RequestBody ReservationAdminCreateRequest reservationAdminCreateRequest) {
        ReservationResponse reservationResponse = reservationService.createAdminReservation(
                reservationAdminCreateRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @GetMapping("/reservation/search")
    public ResponseEntity<List<ReservationResponse>> showSearchedReservations(
            @RequestParam Long themeId,
            @RequestParam Long memberId,
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo) {
        List<ReservationResponse> searchedReservations =
                reservationService.findSearchedReservations(themeId, memberId, dateFrom, dateTo);

        return ResponseEntity.ok().body(searchedReservations);
    }

    @GetMapping("/time")
    public String showReservationTimePage() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String showThemePage() {
        return "admin/theme";
    }
}
