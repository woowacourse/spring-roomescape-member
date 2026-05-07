package roomescape.reservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.response.ReservationDetailDto;
import roomescape.reservation.dto.request.ReservationSaveDto;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/admin")
public class ReservationAdminController {

    private final ReservationService reservationService;

    public ReservationAdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationDetailDto>> getReservations() {
        List<ReservationDetailDto> responseData = reservationService.readAll().stream()
                .map(ReservationDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationDetailDto> createReservation(@Validated @RequestBody ReservationSaveDto dto) {
        Reservation reservation = reservationService.reserve(dto);
        ReservationDetailDto responseData = ReservationDetailDto.from(reservation);
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/reservations/{id}")
    public ResponseEntity<ReservationDetailDto> cancelReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.cancel(id);
        ReservationDetailDto responseData = ReservationDetailDto.from(reservation);
        return ResponseEntity.ok(responseData);
    }

}
