package roomescape.controller.user;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationUpdateRequest;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> register(@Valid @RequestBody ReservationRequest reservationRequest) {
        ReservationResponse reservationResponse = reservationService.register(reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readAllByName(@RequestParam String username) {
        List<ReservationResponse> reservationResponses = reservationService.readAllByName(username);
        return ResponseEntity.ok(reservationResponses);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> cancelByIdAndName(@PathVariable Long id, @RequestParam String username) {
        reservationService.cancelByIdAndName(id, username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<ReservationResponse> update(@PathVariable Long id,
                                                      @RequestParam String username,
                                                      @Valid @RequestBody ReservationUpdateRequest reservationUpdateRequest) {
        ReservationResponse reservationResponse = reservationService.update(id, username, reservationUpdateRequest);
        return ResponseEntity.ok().body(reservationResponse);
    }
}
