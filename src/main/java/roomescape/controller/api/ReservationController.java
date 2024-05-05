package roomescape.controller.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.domain.RankTheme;
import roomescape.domain.Reservation;
import roomescape.controller.request.ReservationRequest;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody ReservationRequest request) {
        Reservation createdReservation = reservationService.save(request);
        return ResponseEntity.created(URI.create("/reservations/" + createdReservation.id())).body(createdReservation);
    }

    @PostMapping("/validate")
    public ResponseEntity<Reservation> createAndValidateFuture(@RequestBody ReservationRequest request) {
        Reservation createdReservation = reservationService.validateFutureAndSave(request);
        return ResponseEntity.created(URI.create("/reservations/" + createdReservation.id())).body(createdReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // TODO: ThemeController로 이동, 이름 변경 rank -> trending
    @GetMapping("/rank")
    public ResponseEntity<List<RankTheme>> getRanking(){
        return ResponseEntity.ok(reservationService.getRanking());
    }
}
