package roomescape.presentation.reservation;

import jakarta.validation.Valid;
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
import roomescape.application.reservation.ReservationService;
import roomescape.application.reservation.dto.request.ReservationRequest;
import roomescape.application.reservation.dto.response.ReservationResponse;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAll() {
        List<ReservationResponse> responses = service.findAll();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody @Valid ReservationRequest request) {
        ReservationResponse response = service.create(request);
        URI location = URI.create("/reservations/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
