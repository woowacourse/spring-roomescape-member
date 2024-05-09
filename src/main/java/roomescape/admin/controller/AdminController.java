package roomescape.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.dto.AdminReservationDto;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationResponseDto;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponseDto> save(@RequestBody AdminReservationDto adminReservationDto) {
        final Reservation reservation = reservationService.create(adminReservationDto.toReservation());

        final ReservationResponseDto reservationResponseDto = changeToReservationResponseDto(reservation);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationResponseDto);
    }

    private ReservationResponseDto changeToReservationResponseDto(final Reservation reservation) {
        return new ReservationResponseDto(reservation);
    }
}
