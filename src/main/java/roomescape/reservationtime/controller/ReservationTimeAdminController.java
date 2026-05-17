package roomescape.reservationtime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.controller.dto.ReservationTimeCreateRequest;
import roomescape.reservationtime.controller.dto.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.reservationtime.service.dto.ReservationTimeResult;

@Tag(name = "admin-reservation-time", description = "예약 시간 관리자 API")
@RestController
@RequestMapping("/admin/themes")
@RequiredArgsConstructor
public class ReservationTimeAdminController {

    private final ReservationTimeService reservationTimeService;

    @GetMapping("/{themeId}/times")
    @Operation(summary = "테마별 예약 시간 조회", description = "관리자 페이지에서 특정 테마의 모든 예약 시간을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약 시간 조회 성공")
    })
    public List<ReservationTimeResponse> read(@PathVariable Long themeId) {
        return reservationTimeService.findAllByThemeId(themeId).stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @PostMapping("/{themeId}/times")
    @Operation(summary = "예약 시간 생성", description = "특정 테마에 새 예약 시간을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "예약 시간 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검증 실패"),
            @ApiResponse(responseCode = "404", description = "테마가 존재하지 않음"),
            @ApiResponse(responseCode = "409", description = "중복 예약 시간")
    })
    public ResponseEntity<ReservationTimeResponse> create(
            @PathVariable Long themeId,
            @Valid @RequestBody ReservationTimeCreateRequest request
    ) {
        ReservationTimeResult reservationTimeResult =
                reservationTimeService.save(request.startAt(), themeId);

        URI location = URI.create("/admin/themes/" + themeId + "/times/" + reservationTimeResult.id());

        return ResponseEntity.created(location)
                .body(ReservationTimeResponse.from(reservationTimeResult));
    }

    @DeleteMapping("/times/{timeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "예약 시간 삭제", description = "관리자가 예약 시간 ID로 예약 시간을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "예약 시간 삭제 성공"),
            @ApiResponse(responseCode = "409", description = "해당 시간에 예약이 존재하여 삭제 불가")
    })
    public void delete(@PathVariable Long timeId) {
        reservationTimeService.deleteById(timeId);
    }

}
