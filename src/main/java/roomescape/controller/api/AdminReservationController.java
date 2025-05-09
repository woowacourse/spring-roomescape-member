package roomescape.controller.api;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.annotation.AdminOnly;
import roomescape.controller.dto.request.CreateReservationForAdminRequest;
import roomescape.controller.dto.response.ReservationResponse;
import roomescape.service.ReservationService;
import roomescape.service.dto.request.ReservationCreation;
import roomescape.service.dto.request.ReservationCriteriaCreation;
import roomescape.service.dto.response.ReservationResult;

@AdminOnly
@RestController
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/admin/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse addReservationFromAdmin(@RequestBody @Valid CreateReservationForAdminRequest request) {
        final ReservationCreation creation = ReservationCreation.from(request);
        ReservationResult reservationResult = reservationService.addReservation(creation);
        return ReservationResponse.from(reservationResult);
    }
}
