package roomescape.time.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import roomescape.time.domain.ReservationTime;
import roomescape.time.controller.dto.request.ReservationTimeSaveDto;
import roomescape.time.controller.dto.request.ReservationTimeStatusUpdateDto;
import roomescape.time.controller.dto.response.ReservationTimeDetailDto;
import roomescape.time.service.ReservationTimeService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ReservationTimeAdminController {

    private final ReservationTimeService reservationTimeService;

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeDetailDto>> getReservationTimes() {
        List<ReservationTimeDetailDto> responseData = reservationTimeService.readAll().stream()
                .map(ReservationTimeDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeDetailDto> create(
            @Validated @RequestBody ReservationTimeSaveDto reservationTimeSaveDto
    ) {
        ReservationTime reservationTime = reservationTimeService.register(reservationTimeSaveDto);
        ReservationTimeDetailDto responseData = ReservationTimeDetailDto.from(reservationTime);
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/times/{id}/status")
    public ResponseEntity<ReservationTimeDetailDto> updateStatus(
            @PathVariable Long id, @Validated @RequestBody ReservationTimeStatusUpdateDto dto
    ) {
        ReservationTime reservationTime = reservationTimeService.updateStatus(id, dto.isActive());
        ReservationTimeDetailDto responseData = ReservationTimeDetailDto.from(reservationTime);
        return ResponseEntity.ok(responseData);
    }

}
