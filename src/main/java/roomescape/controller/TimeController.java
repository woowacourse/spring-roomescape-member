package roomescape.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.dto.request.ReservationTimeCreateRequest;
import roomescape.dto.response.ReservationTimeCreateResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
@Tag(name = "예약 시간 관리", description = "예약 가능 시간 관리 API")
public class TimeController {

    private final ReservationTimeService reservationTimeService;

    public TimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    @Operation(
            summary = "모든 예약 시간 조회",
            description = "사용 가능한 모든 예약 시간을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "예약 시간 목록 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationTimeResponse.class))
                    )
            }
    )
    public List<ReservationTimeResponse> findAll() {
        return reservationTimeService.findAll();
    }

    @PostMapping
    @Operation(
            summary = "예약 시간 생성",
            description = "새로운 예약 가능 시간을 생성합니다. 시간은 'HH:mm' 형식(예: '13:00')으로 입력합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "예약 시간 생성 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationTimeCreateResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 시간 형식"
                    )
            }
    )
    public ResponseEntity<ReservationTimeCreateResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "예약 시간 생성 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReservationTimeCreateRequest.class))
            )
            @RequestBody ReservationTimeCreateRequest reservationTimeCreateRequest) {
        ReservationTimeCreateResponse response = reservationTimeService.create(reservationTimeCreateRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "예약 시간 삭제",
            description = "ID로 예약 시간을 삭제합니다. 해당 시간에 예약이 있으면 삭제할 수 없습니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "예약 시간 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "예약 시간을 찾을 수 없음"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "해당 시간에 예약이 존재함"
                    )
            }
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "삭제할 예약 시간 ID") @PathVariable Long id) {
        reservationTimeService.deleteIfNoReservation(id);
        return ResponseEntity.noContent().build();
    }
}
