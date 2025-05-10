package roomescape.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.dto.request.ReservationAdminCreateRequest;
import roomescape.dto.response.ReservationCreateResponse;
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
}
