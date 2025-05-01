package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.dto.ReservationTimeAvailableResponse;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.service.ReservationTimeService;

@Controller
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/admin/time")
    public String displayAdminTime() {
        return "/admin/time";
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponseDto>> getTimes(
    ) {
        List<ReservationTimeResponseDto> reservationTimes = reservationTimeService.findAllTimes();
        return ResponseEntity.ok().body(reservationTimes);
    }

    @GetMapping("/times/available")
    public ResponseEntity<List<ReservationTimeAvailableResponse>> getAvailableTimes(
            @RequestParam LocalDate date,
            @RequestParam Long themeId
    ) {
        List<ReservationTimeAvailableResponse> reservationTimes = reservationTimeService.findAvailableTimes(date,
                themeId);
        return ResponseEntity.ok().body(reservationTimes);
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponseDto> createTime(
            @RequestBody ReservationTimeRequestDto timeRequest
    ) {
        ReservationTimeResponseDto reservationTime = reservationTimeService.createTime(timeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationTime);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteTime(
            @PathVariable("id") long idRequest
    ) {
        reservationTimeService.deleteTime(idRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
