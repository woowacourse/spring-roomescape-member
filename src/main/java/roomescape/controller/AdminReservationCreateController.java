package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class AdminReservationCreateController {
    private final ReservationService reservationService;

    public AdminReservationCreateController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(params = {"memberId", "themeId", "dateFrom", "dateTo"})
    public ResponseEntity<List<ReservationResponse>> findAllByMemberAndThemeAndPeriod(
            @RequestParam(required = false, name = "memberId") final Long memberId,
            @RequestParam(required = false, name = "themeId") final Long themeId,
            @RequestParam(required = false, name = "dateFrom") final String dateFrom,
            @RequestParam(required = false, name = "dateTo") final String dateTo) {
        return ResponseEntity.ok(
                reservationService.findDistinctReservations(memberId, themeId, dateFrom, dateTo));
    }
}
