package roomescape.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    // TODO: POST /member/reservations - 사용자 예약 생성

    @GetMapping("/reservations/{name}")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(@PathVariable String name) {
        List<ReservationResponse> responseData = reservationService.readAllByName(name);
        return ResponseEntity.ok(responseData);
    }

    // TODO: PATCH /member/reservations/{id} - 사용자 예약 상태 변경 (취소)
}
