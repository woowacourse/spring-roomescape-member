package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.service.RoomescapeService;

@RestController
public class ReservationTimeController {

    private final RoomescapeService roomescapeService;

    public ReservationTimeController(final RoomescapeService roomescapeService) {
        this.roomescapeService = roomescapeService;
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> reservationTimeList() {
        return ResponseEntity.ok(roomescapeService.findReservationTimes());
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> reservationTimeAdd(@RequestBody ReservationTimeRequest request) {
        return ResponseEntity.ok(roomescapeService.addReservationTime(request));
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> reservationTimeRemove(@PathVariable long id) {
        roomescapeService.removeReservationTime(id);
        return ResponseEntity.ok().build();
    }
}
