package roomescape.controller.admin;

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
import roomescape.domain.Reservation;
import roomescape.dto.other.ReservationCreationContent;
import roomescape.dto.other.ReservationSearchCondition;
import roomescape.dto.request.ReservationCreationAdminRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController(value = "AdminReservationController")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> getReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }

    @GetMapping(value = "/reservations", params = {"themeId", "memberId", "dateFrom", "dateTo"})
    public List<ReservationResponse> getReservationsByFilter(
            @Valid @ModelAttribute ReservationSearchCondition condition
    ) {
        List<Reservation> reservations = reservationService.getAllReservationsByFilter(condition);
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationCreationAdminRequest request
    ) {
        ReservationCreationContent creationContent = new ReservationCreationContent(request);
        long id = reservationService.saveReservation(creationContent);
        Reservation savedReservation = reservationService.getReservationById(id);
        return ResponseEntity
                .created(URI.create("/reservations/" + id))
                .body(new ReservationResponse(savedReservation));
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long id
    ) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
