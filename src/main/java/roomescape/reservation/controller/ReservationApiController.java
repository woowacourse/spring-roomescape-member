package roomescape.reservation.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.controller.request.ReservationCreateRequest;
import roomescape.reservation.controller.response.ReservationResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.user.controller.dto.ReservationRequest;

@RestController
@RequestMapping("/admin/reservations")
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> searchReservations(
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) Long themeId,
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end
    ) {

        if (memberId == null && themeId == null && start == null && end == null) {
            return ResponseEntity.ok(reservationService.getAll());
        }

        List<ReservationResponse> reservationResponses = reservationService.searchReservations(memberId, themeId, start,
                end);
        return ResponseEntity.ok(reservationResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationCreateRequest request) {
        ReservationResponse response = reservationService.create(request.memberId(), new ReservationRequest(
                request.date(), request.themeId(), request.timeId()
        ));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
