package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.AvailableReservationTimeResponseDto;
import roomescape.dto.ReservationTimeCreateRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.service.ReservationTimeService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponseDto>> getAllReservationTimes() {
        List<ReservationTimeResponseDto> allReservationTimeResponses = reservationTimeService.findAllReservationTimes();
        return ResponseEntity.ok(allReservationTimeResponses);
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableReservationTimeResponseDto>> getAvailableReservationTimes(@RequestParam("date") LocalDate date, @RequestParam("themeId") Long themeId) {
        List<AvailableReservationTimeResponseDto> allReservationTimeResponses = reservationTimeService.findAvailableReservationTimes(date,themeId);
        return ResponseEntity.ok(allReservationTimeResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponseDto> postReservationTime(@RequestBody final ReservationTimeCreateRequestDto requestDto) {
        ReservationTimeResponseDto responseDto = reservationTimeService.createReservationTime(requestDto);
        return ResponseEntity.created(URI.create("times/" + responseDto.id())).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeService.deleteReservationTimeById(id);
        return ResponseEntity.noContent().build();
    }
}
