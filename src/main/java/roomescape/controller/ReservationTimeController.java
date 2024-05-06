package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.dto.time.BookableTimeResponse;
import roomescape.dto.time.TimeRequest;
import roomescape.dto.time.TimeResponse;
import roomescape.service.ReservationTimeService;

@Controller
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<TimeResponse> insertTime(@RequestBody TimeRequest timeRequest) {
        TimeResponse response = reservationTimeService.insertReservationTime(timeRequest);
        return ResponseEntity.created(URI.create("/times/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TimeResponse>> getTimes() {
        List<TimeResponse> reservationTimes = reservationTimeService.getAllReservationTimes();
        return ResponseEntity.ok().body(reservationTimes);
    }

    @GetMapping("/{date}/{themeId}")
    public ResponseEntity<List<BookableTimeResponse>> getTimesByDateAndTheme(@PathVariable String date,
                                                                             @PathVariable Long themeId) {
        List<BookableTimeResponse> bookableTimes = reservationTimeService.getAllBookableTimes(date, themeId);
        return ResponseEntity.ok().body(bookableTimes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
        return ResponseEntity.badRequest().body("시간을 선택해야 합니다.");
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException() {
        return ResponseEntity.badRequest().body("예약이 존재하는 시간은 삭제할 수 없습니다.");
    }
}
