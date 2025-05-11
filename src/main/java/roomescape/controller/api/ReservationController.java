package roomescape.controller.api;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.request.CreateReservationRequest;
import roomescape.controller.dto.response.ReservationResponse;
import roomescape.domain.LoginMember;
import roomescape.service.ReservationService;
import roomescape.service.dto.request.ReservationCreation;
import roomescape.service.dto.response.ReservationResult;

@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse addReservation(@RequestBody @Valid CreateReservationRequest request,
                                              LoginMember member) {
        final ReservationCreation creation = ReservationCreation.of(member, request);
        ReservationResult reservationResult = reservationService.addReservation(creation);
        return ReservationResponse.from(reservationResult);
    }

    @GetMapping
    public List<ReservationResponse> findAllReservations() {
        return reservationService.getAllReservations()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeReservation(@PathVariable long id) {
        reservationService.removeReservationById(id);
    }
}
