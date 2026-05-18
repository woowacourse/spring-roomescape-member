package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationTimeResponseDTO;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponseDTO>> readAll() {
        return ResponseEntity
                .ok(reservationTimeService.findAllReservationTime());
    }

    @GetMapping("/reserved")
    public ResponseEntity<List<ReservationTimeResponseDTO>> readReservedTimesByThemeIdAndDate(
            @RequestParam Long themeId,
            @RequestParam LocalDate selectedDate
    ) {
        return ResponseEntity
                .ok(reservationTimeService.findReservedTimes(selectedDate, themeId));
    }
}
