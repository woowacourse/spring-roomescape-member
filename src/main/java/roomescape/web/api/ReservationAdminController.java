package roomescape.web.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationService;
import roomescape.service.dto.request.ReservationRequest;
import roomescape.service.dto.response.ReservationResponse;
import roomescape.web.api.dto.ReservationAdminRequest;
import roomescape.web.api.dto.ReservationListResponse;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationAdminController {
    private final ReservationService reservationService;

    public ReservationAdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> save(@RequestBody @Valid ReservationAdminRequest request) {
        ReservationRequest reservationRequest = new ReservationRequest(request.memberId(), request.date(),
                request.timeId(), request.themeId());

        ReservationResponse reservationResponse = reservationService.save(reservationRequest);

        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @GetMapping("/admin/reservations")
    public ResponseEntity<ReservationListResponse> findByCondition(
            @RequestParam(value = "memberId") Long memberId,
            @RequestParam(value = "themeId") Long themeId,
            @RequestParam(value = "dateFrom") String dateFrom,
            @RequestParam(value = "dateTo") String dateTo
    ) {
        List<ReservationResponse> reservationResponses = reservationService.findByCondition(memberId, themeId, dateFrom,
                dateTo);

        return ResponseEntity.ok(new ReservationListResponse(reservationResponses));
    }
}
