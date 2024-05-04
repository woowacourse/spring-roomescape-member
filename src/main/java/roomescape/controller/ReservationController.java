package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationCreateRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readReservations() {
        List<ReservationResponse> response = service.readReservations();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationCreateRequest dto) {
        ReservationResponse response = service.createReservation(dto);

        URI location = URI.create("/reservations/" + response.id());
        return ResponseEntity
                .created(location)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        service.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
