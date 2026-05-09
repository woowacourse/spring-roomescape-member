package roomescape.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@Tag(name = "사용자 - 예약 시간 조회", description = "사용자용 예약 시간 목록 조회 API")
@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @Operation(summary = "전체 예약 시간 목록 조회", description = "등록된 모든 예약 시간 슬롯을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "예약 시간 목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> readTimes() {
        return ResponseEntity.ok(reservationTimeService.getReservationTimes().stream()
                .map(ReservationTimeResponse::from)
                .toList());
    }
}
