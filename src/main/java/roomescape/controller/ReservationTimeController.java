package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationTimeAvailabilityResponseDto;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping(value = "/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<ReservationTimeAvailabilityResponseDto>> readAvailabilityByDateAndTheme(
            @RequestParam("date") LocalDate date, @RequestParam("themeId") Long themeId) {

        List<ReservationTimeAvailabilityResponseDto> responseDtos = reservationTimeService.readAvailabilityByDateAndTheme(
                date, themeId);

        return ResponseEntity.ok(responseDtos);
    }
}
