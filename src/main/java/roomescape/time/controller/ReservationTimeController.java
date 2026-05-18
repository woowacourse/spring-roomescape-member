package roomescape.time.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.dto.response.ReservationTimeDetailDto;
import roomescape.time.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    @Operation(summary = "Read available reservation times", description = "예약 가능한 시간을 조회하는 api")
    public ResponseEntity<List<ReservationTimeDetailDto>> getAvailableTimes(@RequestParam("date") LocalDate date, @RequestParam("themeId") Long themeId){
        List<ReservationTimeDetailDto> responseData = reservationTimeService.findAvailableTimes(date, themeId).stream()
                .map(ReservationTimeDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }
}
