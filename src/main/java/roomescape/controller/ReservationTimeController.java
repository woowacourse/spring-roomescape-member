package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.ReservationTime;
import roomescape.dto.time.TimeRequest;
import roomescape.dto.time.BookableTimeResponse;
import roomescape.service.ReservationTimeService;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTime> insertTime(@RequestBody TimeRequest timeRequest) {
        ReservationTime reservationTime = reservationTimeService.insertReservationTime(timeRequest);
        return ResponseEntity.created(URI.create("/times/" + reservationTime.getId())).body(reservationTime);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTime>> getTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.getAllReservationTimes();
        return ResponseEntity.ok().body(reservationTimes);
    }

    @GetMapping("/{date}/{themeId}")
    public ResponseEntity<List<BookableTimeResponse>> getTimesByDateAndTheme(@PathVariable String date, @PathVariable Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeService.getAllReservationTimes();
        List<BookableTimeResponse> bookableTimeRespons = reservationTimes.stream()
                .map(time -> new BookableTimeResponse(time.getId(), time.getStartAt(), reservationTimeService.isBooked(date, time.getId(), themeId)))
                .toList();
        return ResponseEntity.ok().body(bookableTimeRespons);
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
