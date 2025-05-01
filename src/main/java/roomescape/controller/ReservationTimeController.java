package roomescape.controller;

import jakarta.validation.Valid;
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
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.service.RoomescapeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final RoomescapeService roomescapeService;

    public ReservationTimeController(final RoomescapeService roomescapeService) {
        this.roomescapeService = roomescapeService;
    }

    @GetMapping()
    public ResponseEntity<List<ReservationTimeResponse>> reservationTimeList() {
        return ResponseEntity.ok(roomescapeService.findReservationTimes());
    }

    @PostMapping()
    public ResponseEntity<ReservationTimeResponse> reservationTimeAdd(
            @RequestBody @Valid ReservationTimeRequest request) {
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(roomescapeService.addReservationTime(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> reservationTimeRemove(@PathVariable(name = "id") long id) {
        roomescapeService.removeReservationTime(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException() {
        return ResponseEntity.badRequest().build();
    }
}
