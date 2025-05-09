package roomescape.presentation;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.ReservationService;
import roomescape.presentation.dto.AdminReservationRequestDto;
import roomescape.presentation.dto.ReservationResponseDto;

@RestController
public final class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponseDto> createReservation(@Valid @RequestBody AdminReservationRequestDto reservationRequestDto) {
        Long id = reservationService.createReservation(reservationRequestDto);
        ReservationResponseDto reservation = reservationService.getReservationById(id);
        String location = "/reservations/" + id;
        return ResponseEntity.created(URI.create(location)).body(reservation);
    }
}
