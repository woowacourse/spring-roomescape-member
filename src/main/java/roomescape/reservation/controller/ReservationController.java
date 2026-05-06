package roomescape.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.dto.request.ReservationSaveDto;
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
        return ResponseEntity.ok(reservationService.create(dto));
    }

    @GetMapping("/reservations/{name}")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(@PathVariable String name) {
        List<ReservationResponse> responseData = reservationService.readAllByName(name);
        return ResponseEntity.ok(responseData);
    }

    // TODO: PATCH /member/reservations/{id} - 사용자 예약 상태 변경 (취소)
}
