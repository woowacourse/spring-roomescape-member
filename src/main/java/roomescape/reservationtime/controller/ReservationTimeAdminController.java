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
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;

@RestController
@RequestMapping("/admin/themes/{themeId}/times")
public class ReservationTimeAdminController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeAdminController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTime>> read(@PathVariable final Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeService.findAllByThemeId(themeId);
        return ResponseEntity.ok()
                .body(reservationTimes);
    }

    @PostMapping
    public ResponseEntity<ReservationTime> create(
            @PathVariable final Long themeId,
            @RequestBody final ReservationTimeCreateRequest request
    ) {
        ReservationTime reservationTime = reservationTimeService.save(request.startAt(), themeId);

        return ResponseEntity.created(URI.create("/admin/themes/" + themeId + "/times/" + reservationTime.getId()))
                .body(reservationTime);
    }

    @DeleteMapping("/{timeId}")
    public ResponseEntity<Void> delete(@PathVariable final Long themeId,
                                       @PathVariable final Long timeId) {
        reservationTimeService.deleteById(timeId);
        return ResponseEntity.noContent()
                .build();
    }
}
