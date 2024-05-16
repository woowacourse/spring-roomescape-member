package roomescape.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.api.dto.response.AvailableReservationTimesResponse;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeApiController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeApiController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/available")
    public ResponseEntity<AvailableReservationTimesResponse> findAvailableReservationTimes(
            @RequestParam final String date,
            @RequestParam final long themeId) {
        final var outputs = reservationTimeService.findAvailableReservationTimes(date, themeId);
        return ResponseEntity.ok(AvailableReservationTimesResponse.from(outputs));
    }
}
