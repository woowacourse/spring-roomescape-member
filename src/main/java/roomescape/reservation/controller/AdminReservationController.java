package roomescape.reservation.controller;

import java.net.URI;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.request.CreateReservationRequest;
import roomescape.reservation.dto.response.AdminReservationPageResponse;
import roomescape.reservation.dto.response.CreateReservationResponse;
import roomescape.reservation.service.ReservationService;

@RequestMapping("/admin/reservations")
@RestController
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<CreateReservationResponse> createReservationByAdmin(
            @RequestBody CreateReservationRequest createReservationRequest) {
        CreateReservationResponse newReservation = reservationService.addReservation(createReservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + newReservation.id())).body(newReservation);
    }

    @GetMapping
    public ResponseEntity<AdminReservationPageResponse> getReservationsByPage(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long themeId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo) {
        AdminReservationPageResponse reservationsWithTotalPage = reservationService.getReservationsByPage(
                page,
                userId,
                themeId,
                dateFrom,
                dateTo
        );
        return ResponseEntity.ok(reservationsWithTotalPage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable(name = "id") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
