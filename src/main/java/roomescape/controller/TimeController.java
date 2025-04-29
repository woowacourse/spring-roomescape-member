package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.service.ReservationTimeService;

@Controller
public class TimeController {

    private final ReservationTimeService reservationTimeService;

    public TimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/admin/time")
    public String displayAdminTime() {
        return "/admin/time.html";
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponseDto> createTime(
            @RequestBody ReservationTimeRequestDto timeRequest
    ) {
        ReservationTimeResponseDto reservationTime = reservationTimeService.createTime(timeRequest);
        return ResponseEntity.ok().body(reservationTime);
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponseDto>> getTimes(
    ) {
        List<ReservationTimeResponseDto> reservationTimes = reservationTimeService.findAllTimes();
        return ResponseEntity.ok().body(reservationTimes);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteTime(
            @PathVariable("id") long idRequest
    ) {
        reservationTimeService.deleteTime(idRequest);
        return ResponseEntity.ok().build();
    }
}
