package roomescape.controller.api;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.config.AuthenticationPrincipal;
import roomescape.dto.AdminReservationCreateRequest;
import roomescape.dto.ReservationCreateRequest;
import roomescape.dto.ReservationFilterRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
public class ReservationRestController {

    private final ReservationService reservationService;

    public ReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody ReservationCreateRequest request,
                                                      @AuthenticationPrincipal Long id) {
        ReservationResponse reservationResponse = reservationService.save(request, id);

        URI location = URI.create("/reservations/" + reservationResponse.id());
        return ResponseEntity.created(location).body(reservationResponse);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getAll() {
        List<ReservationResponse> responses = reservationService.findAll();

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> adminCreate(
            @Valid @RequestBody AdminReservationCreateRequest adminReservationCreateRequest) {
        ReservationResponse reservationResponse = reservationService.save(adminReservationCreateRequest);

        URI location = URI.create("/admin/reservations/" + reservationResponse.id());
        return ResponseEntity.created(location).body(reservationResponse);
    }

    @GetMapping("/admin/reservations")
    public ResponseEntity<List<ReservationResponse>> getAll(@ModelAttribute ReservationFilterRequest reservationFilterRequest) {
        List<ReservationResponse> reservationResponses = reservationService.findAll(reservationFilterRequest);
        return ResponseEntity.ok(reservationResponses);
    }
}
