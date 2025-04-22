package roomescape.reservation.controller;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private ReservationService reservationService;
    private ReservationDao reservationDao;

    public ReservationController(ReservationDao reservationDao, ReservationService reservationService) {
        this.reservationDao = reservationDao;
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody ReservationRequest reservationRequest
    ) {
        return ResponseEntity.ok().body(
                reservationService.createReservation(reservationRequest)
        );
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getReservations(
    ) {
        return ResponseEntity.ok().body(
                reservationDao.findAllReservations()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long id
    ) {
        reservationDao.delete(id);
        return ResponseEntity.noContent().build();
    }

    private URI createUri(int reservationId) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservationId)
                .toUri();
    }

}
