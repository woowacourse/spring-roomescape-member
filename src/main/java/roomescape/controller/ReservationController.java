package roomescape.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RequestMapping("/api/v1/reservations")
@RestController
@Validated
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<Reservation> reservations = reservationService.getReservations();
        List<ReservationResponse> reservationResponses = ReservationResponse.fromAll(reservations);
        return ResponseEntity.ok().body(reservationResponses);
    }

    @GetMapping(params = "userName")
    public ResponseEntity<List<ReservationResponse>> getUserReservations(
            @Size(min = 2, max = 10)
            @Pattern(regexp = "^[a-zA-Z가-힣]+$")
            @RequestParam String userName
    ) {
        List<Reservation> reservations = reservationService.getUserReservations(userName);
        List<ReservationResponse> reservationResponses = ReservationResponse.fromAll(reservations);
        return ResponseEntity.ok().body(reservationResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationCreateRequest reservationCreateRequest) {
        Reservation savedReservation = reservationService.createReservation(
                reservationCreateRequest.name(),
                reservationCreateRequest.date(),
                reservationCreateRequest.timeId(),
                reservationCreateRequest.themeId()
        );
        ReservationResponse reservationResponse = ReservationResponse.from(savedReservation);
        return ResponseEntity.created(URI.create("/api/v1/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
