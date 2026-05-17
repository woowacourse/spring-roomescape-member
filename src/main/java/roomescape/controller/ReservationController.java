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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.dto.CreateReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.UpdateReservationRequest;
import roomescape.service.ReservationService;

@Tag(name = "사용자 - 예약 관리", description = "사용자용 예약 생성·내 예약 조회·삭제 API")
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "내 예약 목록 조회", description = "사용자 ID에 해당하는 예약 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "내 예약 목록 조회 성공")
    @GetMapping("/my")
    public ResponseEntity<List<ReservationResponse>> readMyReservations(
            @Parameter(description = "사용자 ID", example = "1")
            @RequestParam Long userId) {
        return ResponseEntity.ok(reservationService.getMyReservations(userId).stream()
                .map(ReservationResponse::from)
                .toList());
    }

    @Operation(summary = "예약 생성", description = "새로운 예약을 생성합니다. 동일 날짜·시간·테마의 중복 예약은 불가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "예약 생성 성공"),
            @ApiResponse(responseCode = "409", description = "중복 예약 오류")
    })
    @PostMapping
    public ResponseEntity<Void> createReservation(
            @RequestBody CreateReservationRequest createReservationRequest) {
        Reservation createdReservation = reservationService.createReservation(createReservationRequest);

        URI location = URI.create("/reservations/" + createdReservation.getId());
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "예약 변경", description = "ID로 기존 예약을 변경합니다. 본인의 예약만 변경 가능합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 또는 과거로의 변경 시도"),
            @ApiResponse(responseCode = "403", description = "본인의 예약이 아님"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 예약 ID"),
            @ApiResponse(responseCode = "409", description = "해당 시간에 이미 예약이 존재함")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateReservation(
            @Parameter(description = "변경할 예약 ID", example = "1") @PathVariable Long id,
            @Parameter(description = "요청자 사용자 ID", example = "1") @RequestParam Long userId,
            @RequestBody UpdateReservationRequest request) {
        Reservation updatedReservation = reservationService.updateReservation(id, userId, request);
        return ResponseEntity.ok(ReservationResponse.from(updatedReservation));
    }

    @Operation(summary = "예약 삭제", description = "본인의 예약만 삭제할 수 있습니다. 타인의 예약 삭제 시 403을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "예약 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "본인의 예약이 아님"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 예약 ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @Parameter(description = "삭제할 예약 ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "요청자 사용자 ID", example = "1")
            @RequestParam Long userId) {
        reservationService.deleteMyReservation(id, userId);
        return ResponseEntity.noContent().build();
    }
}
