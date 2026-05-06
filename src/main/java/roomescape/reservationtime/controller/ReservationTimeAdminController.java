package roomescape.reservationtime.controller;

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
@RequestMapping("/admin/themes")
public class ReservationTimeAdminController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeAdminController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/{themeId}/times")
    public ResponseEntity<List<ReservationTime>> read(@PathVariable final Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeService.findAllByThemeId(themeId);
        return ResponseEntity.ok()
                .body(reservationTimes);
    }

    @PostMapping("/{themeId}/times")
    public ResponseEntity<ReservationTime> create(
            @PathVariable final Long themeId,
            @RequestBody final ReservationTimeCreateRequest request
    ) {
        ReservationTime reservationTime = reservationTimeService.save(request.startAt(), themeId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationTime);
    }

    @DeleteMapping("/{timeId}")
    public ResponseEntity<Void> delete(@PathVariable final Long timeId) {
        reservationTimeService.deleteById(timeId);
        return ResponseEntity.noContent()
                .build();
    }
}
