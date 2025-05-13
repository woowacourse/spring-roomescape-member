package roomescape.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import roomescape.support.auth.LoginMember;

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
    public ResponseEntity<List<ReservationResponse>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
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
                    )
            }
    )
    public ResponseEntity<ReservationCreateResponse> create(
            @RequestBody @Valid final ReservationCreateRequest reservationCreateRequest,
            final LoginMember loginMember) {
        final ReservationCreateResponse reservationCreateResponse = reservationService.create(reservationCreateRequest,
                loginMember);

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
                    )
            }
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "삭제할 예약 ID") final @PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
