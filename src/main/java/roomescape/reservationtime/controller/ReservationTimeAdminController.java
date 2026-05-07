package roomescape.reservationtime.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.controller.dto.ReservationTimeCreateRequest;
import roomescape.reservationtime.controller.dto.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;

@RestController
@RequestMapping("/admin/themes/{themeId}/times")
public class ReservationTimeAdminController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeAdminController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes(@PathVariable final Long themeId) {
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.findAllByThemeId(themeId).stream()
                .map(ReservationTimeResponse::from)
                .toList();
        return ResponseEntity.ok()
                .body(reservationTimes);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createReservationTime(
            @PathVariable final Long themeId,
            @RequestBody final ReservationTimeCreateRequest request
    ) {
        ReservationTimeResponse reservationTime = ReservationTimeResponse.from(
                reservationTimeService.save(request.startAt(), themeId)
        );

        return ResponseEntity.created(URI.create("/admin/themes/" + themeId + "/times/" + reservationTime.id()))
                .body(reservationTime);
    }

    @DeleteMapping("/{timeId}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable final Long themeId,
                                                      @PathVariable final Long timeId) {
        reservationTimeService.deleteById(timeId);
        return ResponseEntity.noContent()
                .build();
    }
}
