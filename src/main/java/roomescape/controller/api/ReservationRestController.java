package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.reservation.dto.ReservationCreateRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dto.ReservationUpdateRequest;
import roomescape.service.ReservationService;

import java.net.URI;
import java.util.List;

@RequestMapping("/reservations")
@RestController
public class ReservationRestController {

    private final ReservationService reservationService;

    public ReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody ReservationCreateRequest reservationReq) {
        ReservationResponse newReservation = reservationService.create(reservationReq);
        URI uri = URI.create("/reservations/" + newReservation.getId());
        return ResponseEntity.created(uri).body(newReservation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> read(@PathVariable Long id) {
        ReservationResponse reservationResponse = reservationService.read(id);
        return ResponseEntity.ok(reservationResponse);
    }

    @GetMapping
    public List<ReservationResponse> readAll() {
        return reservationService.readAll();
    }

    @GetMapping("/mine")
    public List<ReservationResponse> readMyReservations(
            @RequestParam String name
    ) {
        return reservationService.readMyReservations(name);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> update(@PathVariable Long id, @Valid @RequestBody ReservationUpdateRequest newReservationReq) {
        ReservationResponse reservationResponse = reservationService.update(id, newReservationReq);
        return ResponseEntity.ok(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
