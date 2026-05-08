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
    public ResponseEntity<List<ReservationTimeResponseDTO>> readReservationTime() {
        return ResponseEntity.ok(reservationTimeService.findAllReservationTime());
    }

    @GetMapping("/booked-times")
    public ResponseEntity<List<ReservationTimeResponseDTO>> findReservedTimes(
            @RequestParam LocalDate selectedDate,
            @RequestParam Long themeId
    ) {
        List<ReservationTimeResponseDTO> reservedTimes = reservationTimeService.findReservedTimes(
                selectedDate, themeId
        );
        return ResponseEntity.ok(reservedTimes);
    }

}
