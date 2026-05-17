package roomescape.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.dto.ResourceIdResponse;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservation.ReservationUpdateRequest;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> getReservations() {
        return reservationService.getReservations().stream()
            .map(ReservationResponse::from)
            .toList();
    }

    @GetMapping(params = "name")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> getReservations(
        @RequestParam String name
    ) {
        return reservationService.getReservations(name).stream()
            .map(ReservationResponse::from)
            .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceIdResponse addReservation(@Valid @RequestBody ReservationRequest requestDto) {
        Reservation reservation = reservationService.addReservation(requestDto);
        return new ResourceIdResponse(reservation.getId());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(
        @RequestParam @NotBlank String name,
        @PathVariable Long id
    ) {
        reservationService.deleteReservation(id, name);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReservation(
        @RequestParam @NotBlank String name,
        @PathVariable Long id,
        @Valid @RequestBody ReservationUpdateRequest request
    ) {
        reservationService.updateDateTime(id, name, request);
    }
}
