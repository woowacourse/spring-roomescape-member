package roomescape.controller.api;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.api.dto.request.ReservationCreateForAdminRequest;
import roomescape.controller.api.dto.response.ReservationResponse;
import roomescape.controller.api.dto.response.ReservationsResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationApiController {

    private final ReservationService reservationService;

    public AdminReservationApiController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody final ReservationCreateForAdminRequest request) {
        final var output = reservationService.createReservation(request.toInput());
        return ResponseEntity.created(URI.create("/reservations/" + output.id()))
                .body(ReservationResponse.from(output));
    }

    @GetMapping
    public ResponseEntity<ReservationsResponse> filterReservations(@RequestParam(required = false) final Long themeId,
                                                                   @RequestParam(required = false) final Long memberId,
                                                                   @RequestParam(required = false) final String dateFrom,
                                                                   @RequestParam(required = false) final String dateTo) {
        final var outputs = reservationService.filterReservations(themeId, memberId, dateFrom, dateTo);
        return ResponseEntity.ok(ReservationsResponse.from(outputs));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable final long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
