package roomescape.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.dto.request.ReservationAdminCreateRequest;
import roomescape.dto.request.ReservationSearchRequest;
import roomescape.dto.response.ReservationCreateResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/admin")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    @Operation(
            summary = "어드민 예약 생성",
            description = "어드민이 새로운 예약을 생성합니다.",
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
            @RequestBody @Valid final ReservationAdminCreateRequest reservationAdminCreateRequest) {

        final ReservationCreateResponse reservationCreateResponse = reservationService.create(
                reservationAdminCreateRequest);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservationCreateResponse.id())
                .toUri();
        return ResponseEntity.created(location).body(reservationCreateResponse);
    }

    @GetMapping("/search")
    @Operation(
            summary = "어드민 필터로 예약 조회",
            description = "어드민이 필터를 통해 예약을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "필터 예약 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReservationResponse.class))
                    )
            }
    )
    public ResponseEntity<List<ReservationResponse>> findByThemeAndMemberAndDate(
            @Valid final ReservationSearchRequest reservationSearchRequest) {
        final List<ReservationResponse> reservations = reservationService.findByThemeAndMemberAndDate(
                reservationSearchRequest);
        return ResponseEntity.ok(reservations);
    }
}
