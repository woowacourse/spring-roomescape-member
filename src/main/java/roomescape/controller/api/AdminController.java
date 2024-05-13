package roomescape.controller.api;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.dto.request.AdminReservationRequestDto;
import roomescape.dto.response.ReservationResponseDto;
import roomescape.service.ReservationService;

@RestController
public class AdminController {
    private final ReservationService reservationService;

    public AdminController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestBody @Valid final AdminReservationRequestDto request
    ) {
        final Reservation reservation = reservationService.createByAdmin(request);
        final ReservationResponseDto response = new ReservationResponseDto(reservation);
        return ResponseEntity.created(URI.create("/admin/reservations" + response.getId())).body(response);
    }
}

