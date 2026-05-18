package roomescape.time.controller;

import static org.springframework.http.HttpStatus.CREATED;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.dto.request.ReservationTimeSaveDto;
import roomescape.time.dto.response.ReservationTimeDetailDto;
import roomescape.time.service.ReservationTimeService;

@Slf4j
@RestController
@RequestMapping("/admin/times")
public class AdminReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public AdminReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    @Operation(summary = "Read reservation times", description = "예약 시간을 조회하는 api")
    public ResponseEntity<List<ReservationTimeDetailDto>> getReservationTimes() {
        List<ReservationTimeDetailDto> responseData = reservationTimeService.findAll().stream()
                .map(ReservationTimeDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @PostMapping
    @Operation(summary = "Create reservation time", description = "예약 시간을 생성하는 api")
    public ResponseEntity<ReservationTimeDetailDto> createReservationTime(
            @Valid @RequestBody ReservationTimeSaveDto reservationTimeSaveDto) {
        ReservationTimeDetailDto responseData = ReservationTimeDetailDto.from(
                reservationTimeService.create(reservationTimeSaveDto.startAt()));
        return ResponseEntity.status(CREATED).body(responseData);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete reservation time", description = "예약 시간을 삭제하는 api")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
