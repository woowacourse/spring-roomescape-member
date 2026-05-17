package roomescape.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.CreateReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@Tag(name = "관리자 - 예약 시간 관리", description = "관리자용 예약 시간 CRUD API")
@RestController
@RequestMapping("/admin/times")
public class AdminReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public AdminReservationTimeController(ReservationTimeService reservationTimeService) {
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

    @Operation(summary = "예약 시간 생성", description = "새로운 예약 시간 슬롯을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "예약 시간 생성 성공")
    @PostMapping
    public ResponseEntity<Void> createTime(@RequestBody CreateReservationTimeRequest createReservationTimeRequest) {
        ReservationTime createdTime = reservationTimeService.createReservationTime(createReservationTimeRequest);
        URI location = URI.create("/admin/times/" + createdTime.getId());
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "예약 시간 삭제", description = "ID로 예약 시간 슬롯을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "예약 시간 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 시간 ID"),
            @ApiResponse(responseCode = "409", description = "해당 시간에 예약이 존재하여 삭제 불가")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(
            @Parameter(description = "삭제할 예약 시간 ID", example = "1")
            @PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
