package roomescape.presentation;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.presentation.request.CreateAdminReservationRequest;
import roomescape.presentation.response.ReservationResponse;
import roomescape.service.ReservationService;
import roomescape.service.param.ReservationSearchParam;
import roomescape.service.result.ReservationResult;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody CreateAdminReservationRequest createAdminReservationRequest) {
        Long reservationId = reservationService.create(createAdminReservationRequest.toServiceParam());
        ReservationResult reservationResult = reservationService.findById(reservationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservationResponse.from(reservationResult));
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam("themeId") Long themeId, @RequestParam("memberId") Long memberId,
                                     @RequestParam("from") LocalDate from, @RequestParam("to") LocalDate to) {
        List<ReservationResult> reservationResults = reservationService.findReservationsBy(
                new ReservationSearchParam(themeId, memberId, from, to));
        List<ReservationResponse> reservationResponses = reservationResults.stream()
                .map(ReservationResponse::from)
                .toList();
        return ResponseEntity.ok(reservationResponses);
    }
}
