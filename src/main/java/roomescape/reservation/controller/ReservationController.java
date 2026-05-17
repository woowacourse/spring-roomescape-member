package roomescape.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.global.auth.UserInfo;
import roomescape.global.auth.annotation.CurrentUser;
import roomescape.reservation.controller.dto.ReservationCreateRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.controller.dto.ReservationUpdateRequest;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.ReservationResult;

@Tag(name = "reservation", description = "예약 API")
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "내 예약 조회", description = "인증된 사용자의 이름으로 예약 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 예약 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @SecurityRequirement(name = "bearerAuth")
    public List<ReservationResponse> readByName(@CurrentUser UserInfo userInfo) {
        return reservationService.getAllByName(userInfo.name()).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @PostMapping
    @Operation(summary = "예약 생성", description = "예약자 이름, 날짜, 시간 ID로 새 예약을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "예약 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검증 실패"),
            @ApiResponse(responseCode = "404", description = "예약 시간이 존재하지 않음"),
            @ApiResponse(responseCode = "409", description = "중복 예약")
    })
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody ReservationCreateRequest request) {
        ReservationResult reservationResult = reservationService.save(
                request.name(),
                request.date(),
                request.timeId()
        );

        URI location = URI.create("/reservations/" + reservationResult.id());

        return ResponseEntity.created(location)
                .body(ReservationResponse.from(reservationResult));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "예약 삭제", description = "인증된 사용자가 본인의 예약을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "예약 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "예약 삭제 권한 없음"),
            @ApiResponse(responseCode = "404", description = "예약이 존재하지 않음")
    })
    public void delete(@PathVariable Long id, @CurrentUser UserInfo userInfo) {
        reservationService.deleteById(id, userInfo.name());
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "예약 수정", description = "인증된 사용자가 본인의 예약 날짜와 시간을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "예약 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효성 검증 실패"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "예약 수정 권한 없음"),
            @ApiResponse(responseCode = "404", description = "예약 또는 예약 시간이 존재하지 않음"),
            @ApiResponse(responseCode = "409", description = "중복 예약")
    })
    @SecurityRequirement(name = "bearerAuth")
    public void update(
            @PathVariable Long id,
            @CurrentUser UserInfo userInfo,
            @Valid @RequestBody ReservationUpdateRequest request
    ) {
        reservationService.update(
                id,
                userInfo.name(),
                request.date(),
                request.timeId()
        );
    }

}
