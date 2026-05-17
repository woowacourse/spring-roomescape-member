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
import roomescape.domain.Reservation;
import roomescape.dto.CreateReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;

@Tag(name = "관리자 - 예약 관리", description = "관리자용 예약 CRUD API")
@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "전체 예약 목록 조회", description = "등록된 모든 예약 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "예약 목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readReservations() {
        return ResponseEntity.ok(reservationService.getReservations().stream()
                .map(ReservationResponse::from)
                .toList());
    }

    @Operation(summary = "예약 단건 조회", description = "ID로 특정 예약 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 예약 ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> readReservation(
            @Parameter(description = "조회할 예약 ID", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ReservationResponse.from(reservationService.getReservation(id)));
    }

    @Operation(summary = "예약 생성", description = "새로운 예약을 생성합니다. 동일 날짜·시간·테마의 중복 예약은 불가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "예약 생성 성공"),
            @ApiResponse(responseCode = "409", description = "중복 예약 오류")
    })
    @PostMapping
    public ResponseEntity<Void> createReservation(@RequestBody CreateReservationRequest createReservationRequest) {
        Reservation createdReservation = reservationService.createReservation(createReservationRequest);
        URI location = URI.create("/admin/reservations/" + createdReservation.getId());
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "예약 삭제", description = "ID로 예약을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "예약 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 예약 ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @Parameter(description = "삭제할 예약 ID", example = "1")
            @PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
