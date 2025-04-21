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
import roomescape.reservation.model.Reservation;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private ReservationDao reservationDao;

    public ReservationController(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getReservations(
    ) {
        return ResponseEntity.ok().body(
                reservationDao.findAllReservations()
        );
    }

    @PostMapping
    public ResponseEntity<Integer> createReservation(
            @RequestBody Reservation reservation
    ) {
        int reservationId = reservationDao.insert(reservation);
        return ResponseEntity.created(createUri(reservationId)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Reservation>> deleteReservation(
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
