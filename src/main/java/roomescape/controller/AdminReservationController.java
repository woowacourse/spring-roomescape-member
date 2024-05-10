package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationService;
import roomescape.service.dto.AdminReservationRequest;
import roomescape.service.dto.ReservationReadRequest;
import roomescape.service.dto.ReservationResponse;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody @Valid final AdminReservationRequest adminReservationRequest) {
        ReservationResponse reservationResponse = reservationService.create(adminReservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @GetMapping("/search")
    public List<ReservationResponse> findReservation(@RequestParam(value = "themeId") long themeId,
                                                     @RequestParam(value = "memberId") long memberId,
                                                     @RequestParam(value = "dateFrom") String dateFrom,
                                                     @RequestParam(value = "dateTo") String dateTo) {
        @Valid ReservationReadRequest reservationReadRequest =
                new ReservationReadRequest(themeId, memberId, dateFrom, dateTo);
        return reservationService.findByThemeAndMemberAndDate(reservationReadRequest);
    }
}
