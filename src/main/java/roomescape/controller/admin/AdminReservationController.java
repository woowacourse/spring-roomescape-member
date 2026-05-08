package roomescape.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.exception.NotFoundException;
import roomescape.policy.AdminReservationSavePolicy;
import roomescape.request.ReservationRequest;
import roomescape.response.ReservationResponse;
import roomescape.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    private static final String DEFAULT_PATH = "/reservations/";
    private final ReservationService reservationService;

    public AdminReservationController(
            ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponse> getReservations() {
        return ReservationResponse.from(reservationService.findAllReservations());
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> saveReservation(@RequestBody ReservationRequest request) {
        Reservation reservationReturned = reservationService.saveReservation(request.toSaveCommand(), new AdminReservationSavePolicy());
        ReservationResponse reservationResponse = ReservationResponse.from(reservationReturned);

        return ResponseEntity.created(getLocation(reservationResponse.id())).body(reservationResponse);
    }

    @NonNull
    private static URI getLocation(Long id) {
        return URI.create(DEFAULT_PATH + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
