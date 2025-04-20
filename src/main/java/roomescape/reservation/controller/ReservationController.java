package roomescape.reservation.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.ReservationResponseDto;
import roomescape.reservation.model.Reservation;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private AtomicLong index = new AtomicLong(1);
    private Map<Long, Reservation> reservations = new ConcurrentHashMap<>();

    @GetMapping
    public ResponseEntity<List<Reservation>> getReservations(
    ) {
        return ResponseEntity.ok().body(
                reservations.values().stream().toList()
        );
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody Reservation reservation
    ) {

        long reservationId = index.getAndIncrement();
        reservations.put(reservationId, reservation);

        return ResponseEntity.ok().body(new ReservationResponseDto(reservationId, reservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Reservation>> deleteReservation(
            @PathVariable Long id
    ) {
        reservations.remove(id);

        return ResponseEntity.ok().build();
    }

}
