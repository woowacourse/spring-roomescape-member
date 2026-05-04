package roomescape.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationCreateResponse;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;
import roomescape.utils.DateTimeConverter;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> search() {
        List<ReservationResponse> responses = reservationService.getReservations();
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping
    public ResponseEntity<ReservationCreateResponse> add(@RequestBody ReservationRequest request) {
        ReservationTime time = new ReservationTime(request.timeId(), null);
        Reservation reservation = new Reservation(
                null,
                request.name(),
                DateTimeConverter.dateConverter(request.date()),
                time
        );

        ReservationCreateResponse response = reservationService.addReservation(reservation);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
    }
}
