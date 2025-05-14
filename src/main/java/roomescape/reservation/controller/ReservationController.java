package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.controller.annotation.LoginMemberId;
import roomescape.auth.controller.annotation.LoginRequired;
import roomescape.reservation.controller.dto.AvailableTimeResponse;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.controller.dto.UserReservationRequest;
import roomescape.reservation.service.ReservationService;

@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponse> getReservations() {
        return reservationService.getAll();
    }

    @GetMapping("/available")
    public List<AvailableTimeResponse> getAvailableTimes(@RequestParam("date") LocalDate date,
                                                         @RequestParam("themeId") Long themeId) {
        return reservationService.getAvailableTimes(date, themeId);
    }

    @LoginRequired
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReservationResponse addReservation(@Valid @RequestBody UserReservationRequest request,
                                              @LoginMemberId Long memberId) {
        return reservationService.add(request.toReservationCreate(memberId));
    }

    @LoginRequired
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{reservationId}")
    public void deleteReservation(@PathVariable("reservationId") Long reservationId) {
        reservationService.remove(reservationId);
    }

}
