package roomescape.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.ReservationSaveDto;
import roomescape.reservation.dto.response.ReservationDetailDto;
import roomescape.reservation.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/member")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationDetailDto> create(@RequestBody ReservationSaveDto dto) {
        Reservation reservation = reservationService.reserve(dto);
        ReservationDetailDto responseData = ReservationDetailDto.from(reservation);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/reservations/{name}")
    public ResponseEntity<List<ReservationDetailDto>> getMyReservations(@PathVariable String name) {
        List<ReservationDetailDto> responseData = reservationService.readAllByName(name).stream()
                .map(ReservationDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/reservations/{id}")
    public ResponseEntity<ReservationDetailDto> cancel(@PathVariable Long id) {
        Reservation reservation = reservationService.cancel(id);
        ReservationDetailDto responseData = ReservationDetailDto.from(reservation);
        return ResponseEntity.ok(responseData);
    }

}
