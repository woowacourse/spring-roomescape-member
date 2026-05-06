package roomescape.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.dto.request.ReservationSaveDto;
import roomescape.reservation.dto.request.ReservationStatusUpdateDto;
import roomescape.reservation.dto.response.ReservationResponse;
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
    public ResponseEntity<ReservationResponse> create(@RequestBody ReservationSaveDto dto) {
        System.out.println(dto.dateId());
        System.out.println(dto.themeId());
        System.out.println(dto.name());
        System.out.println(dto.timeId());
        return ResponseEntity.ok(reservationService.create(dto));
    }

    @GetMapping("/reservations/{name}")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(@PathVariable String name) {
        List<ReservationResponse> responseData = reservationService.readAllByName(name);
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.cancel(id));
    }
}
