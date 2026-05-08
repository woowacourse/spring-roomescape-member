package roomescape.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private static final String DEFAULT_PATH = "/reservations/";
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations(@RequestParam("name") String name) {
        try {
            List<ReservationResponse> responses = ReservationResponse.from(reservationService.findReservationsByName(name));
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> saveReservation(@RequestBody ReservationRequest request) {
        try {
            Reservation reservationReturned = reservationService.saveUserReservation(request.toSaveCommand());
            ReservationResponse reservationResponse = ReservationResponse.from(reservationReturned);

            return ResponseEntity.created(getLocation(reservationResponse.id())).body(reservationResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @NonNull
    private static URI getLocation(Long id) {
        return URI.create(DEFAULT_PATH + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
