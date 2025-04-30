package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.model.ReservationTime;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNullStartAtException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("[ERROR] "+e.getMessage());
    }

    @PostMapping()
    public ResponseEntity<ReservationTime> addTime(@RequestBody ReservationTimeRequestDto reservationTimeRequestDto) {
        ReservationTime reservationTime = reservationTimeService.addTime(reservationTimeRequestDto.startAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationTime);
    }

    @GetMapping()
    public ResponseEntity<List<ReservationTime>> getAllTime() {
        return ResponseEntity.ok(reservationTimeService.getAllTime());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        reservationTimeService.deleteTime(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();


    }


}
