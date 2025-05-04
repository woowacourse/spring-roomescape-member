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
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.ReservationCreateResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
@Tag(name = "예약 관리", description = "예약 조회, 생성, 삭제 API")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @Operation(
            summary = "전체 예약 조회",
            description = "모든 예약 정보를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "예약 정보 목록 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationResponse.class))
                    )
            }
    )
    public List<ReservationResponse> findAll() {
        return reservationService.findAll();
    }

    @PostMapping
    @Operation(
            summary = "예약 생성",
            description = "새로운 예약을 생성합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "예약 생성 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationCreateResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 형식 또는 유효하지 않은 데이터"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "이미 해당 테마, 시간에 존재하는 예약"
                    )
            }
    )
    public ResponseEntity<ReservationCreateResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "예약 생성 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReservationCreateRequest.class))
            )
            @RequestBody final ReservationCreateRequest reservationCreateRequest) {
        final ReservationCreateResponse reservationCreateResponse = reservationService.create(reservationCreateRequest);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservationCreateResponse.id())
                .toUri();
        return ResponseEntity.created(location).body(reservationCreateResponse);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "예약 삭제",
            description = "ID로 예약을 삭제합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "예약 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "예약 정보를 찾을 수 없음"
                    )
            }
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "삭제할 예약 ID") final @PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
