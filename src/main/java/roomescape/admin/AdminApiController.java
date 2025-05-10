package roomescape.admin;

import static roomescape.reservation.controller.response.ReservationSuccessCode.RESERVE;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.global.response.ApiResponse;
import roomescape.reservation.controller.response.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminApiController {

    private final ReservationService reservationService;

    @PostMapping("/reservations")
    public ResponseEntity<ApiResponse<ReservationResponse>> reserve(
            @RequestBody @Valid ReserveByAdminRequest request
    ) {
        ReservationResponse response = reservationService.reserve(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(RESERVE, response));
    }
}
