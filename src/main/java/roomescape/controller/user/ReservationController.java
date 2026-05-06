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
import roomescape.exception.NotFoundException;
import roomescape.request.ReservationRequest;
import roomescape.response.ReservationResponse;
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
    public List<ReservationResponse> getReservations(@RequestParam String name) {

        return ReservationResponse.from(reservationService.findAllReservations());
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> saveReservation(@RequestBody ReservationRequest request) {
        try {
            Reservation reservationReturned = reservationService.saveReservation(request.toSaveCommand());
            ReservationResponse reservationResponse = ReservationResponse.from(reservationReturned);

            return ResponseEntity.created(getLocation(reservationResponse.id())).body(reservationResponse);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @NonNull
    private static URI getLocation(Long id) {
        return URI.create(DEFAULT_PATH + id);
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);
    }
}
