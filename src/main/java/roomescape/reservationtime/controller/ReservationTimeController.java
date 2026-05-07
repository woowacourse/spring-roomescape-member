package roomescape.reservationtime.controller;

import java.util.List;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.controller.dto.ReservationTimeRequest;
import roomescape.reservationtime.controller.dto.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;

@RestController
@RequestMapping("/themes/{themeId}/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<ReservationTimeResponse>> getAvailableTimes(
            @PathVariable final Long themeId,
            @ModelAttribute final ReservationTimeRequest themeReservationTimeRequest
    ) {
        return ResponseEntity.ok()
                .body(reservationTimeService.findAvailableTimes(themeReservationTimeRequest.date(), themeId).stream()
                        .map(ReservationTimeResponse::from)
                        .toList());
    }
}
