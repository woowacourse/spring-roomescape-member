package roomescape.reservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.controller.dto.AdminReservationRequest;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/reservations")
    public List<ReservationResponse> searchReservations(@RequestParam Long themeId,
                                                        @RequestParam Long memberId,
                                                        @RequestParam LocalDate dateFrom,
                                                        @RequestParam LocalDate dateTo) {
        return reservationService.getFilteredReservations(themeId, memberId, dateFrom, dateTo);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/reservations")
    public ReservationResponse addAdminReservation(@RequestBody AdminReservationRequest adminRequest) {
        ReservationRequest reservationRequest = new ReservationRequest(adminRequest.date(), adminRequest.timeId(),
                adminRequest.themeId(), adminRequest.memberId());
        return reservationService.add(reservationRequest);
    }

}

