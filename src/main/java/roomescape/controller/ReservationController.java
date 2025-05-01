package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.RoomescapeService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final RoomescapeService roomescapeService;

    public ReservationController(final RoomescapeService roomescapeService) {
        this.roomescapeService = roomescapeService;
    }

    @GetMapping()
    public ResponseEntity<List<ReservationResponse>> reservationList() {
        return ResponseEntity.ok(roomescapeService.findReservations());
    }

    @PostMapping()
    public ResponseEntity<ReservationResponse> reservationAdd(@RequestBody ReservationRequest request) {
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(roomescapeService.addReservation(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> reservationRemove(@PathVariable(name = "id") long id) {
        roomescapeService.removeReservation(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException() {
        return ResponseEntity.notFound().build();
    }
}
