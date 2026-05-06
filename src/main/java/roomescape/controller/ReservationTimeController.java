package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationTimeListResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;


@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<ReservationTimeListResponse> list() {
        return ResponseEntity.ok(ReservationTimeListResponse.from(
                reservationTimeService.findAll()
                        .stream()
                        .map(ReservationTimeResponse::from)
                        .toList()));
    }

}
