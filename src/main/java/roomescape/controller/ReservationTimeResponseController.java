package roomescape.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.service.ReservationTimeService;

import java.net.URI;
import java.util.List;

@Controller
public class ReservationTimeResponseController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeResponseController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTime> insertTime(@RequestBody ReservationTimeRequestDto reservationTimeRequestDto) {
        ReservationTime reservationTime = reservationTimeService.insertReservationTime(reservationTimeRequestDto);
        return ResponseEntity.created(URI.create("/times/" + reservationTime.getId())).body(reservationTime);
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTime>> getTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.getAllReservationTimes();
        return ResponseEntity.ok().body(reservationTimes);
    }

    @GetMapping("/times/filter")
    public ResponseEntity<List<ReservationTimeResponseDto>> getTimesByDateAndTheme(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam String date, @RequestParam Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeService.getAllReservationTimes();
        List<ReservationTimeResponseDto> timeResponses = reservationTimes.stream()
                .map(time -> new ReservationTimeResponseDto(time.getId(), time.getStartAt(), reservationTimeService.isBooked(date, time.getId(), themeId)))
                .toList();
        return ResponseEntity.ok().body(timeResponses);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
