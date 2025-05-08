package roomescape.controller.api;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.request.CreateReservationRequest;
import roomescape.controller.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationApiController {

    private final ReservationService service;

    public ReservationApiController(final ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> reserve(@Valid final CreateReservationRequest request) {
        var reservation = service.reserve(request.name(), request.date(), request.timeId(), request.themeId());
        var response = ReservationResponse.from(reservation);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        var reservations = service.findAllReservations();
        var response = ReservationResponse.from(reservations);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        boolean isRemoved = service.removeById(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
