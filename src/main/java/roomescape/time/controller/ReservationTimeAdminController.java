package roomescape.time.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.request.ReservationTimeSaveDto;
import roomescape.time.dto.response.ReservationTimeDetailDto;
import roomescape.time.service.ReservationTimeService;

@RestController
@RequestMapping("/admin")
public class ReservationTimeAdminController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeAdminController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeDetailDto>> read() {
        return ResponseEntity.ok(reservationTimeService.findAll());
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeDetailDto> create(
            @RequestBody ReservationTimeSaveDto reservationTimeSaveDto
    ) {
        ReservationTime reservationTime = reservationTimeService.register(reservationTimeSaveDto);
        ReservationTimeDetailDto responseData = ReservationTimeDetailDto.from(reservationTime);
        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<ReservationTimeDetailDto> delete(@PathVariable Long id) {
        return ResponseEntity.ok(reservationTimeService.delete(id));
    }
}
