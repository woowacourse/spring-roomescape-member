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
@RequestMapping("/admin")
public class ReservationTimeAdminController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeAdminController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    @Operation(summary = "Read reservation times", description = "예약 시간을 조회하는 api")
    public ResponseEntity<List<ReservationTimeDetailDto>> read() {
        return ResponseEntity.ok(reservationTimeService.findAll());
    }

    @PostMapping("/times")
    @Operation(summary = "Create reservation time", description = "예약 시간을 생성하는 api")
    public ResponseEntity<ReservationTimeDetailDto> create(
            @Valid @RequestBody ReservationTimeSaveDto reservationTimeSaveDto) {
        return ResponseEntity.status(CREATED).body(reservationTimeService.create(reservationTimeSaveDto));
    }

    @DeleteMapping("/times/{id}")
    @Operation(summary = "Delete reservation time", description = "예약 시간을 삭제하는 api")
    public ResponseEntity<ReservationTimeDetailDto> delete(@PathVariable Long id) {
        return ResponseEntity.ok(reservationTimeService.delete(id));
    }
}
