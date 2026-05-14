package roomescape.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservationtime.dto.ReservationTimeCreateRequest;
import roomescape.domain.reservationtime.dto.ReservationTimeResponse;
import roomescape.domain.reservationtime.dto.ReservationTimeUpdateRequest;
import roomescape.service.ReservationTimeService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
public class ReservationTimeRestController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeRestController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping("/admin/times")
    public ResponseEntity<ReservationTimeResponse> create(@RequestBody ReservationTimeCreateRequest reservationTimeReq) {
        ReservationTimeResponse newReservationTime = reservationTimeService.create(reservationTimeReq);
        URI uri = URI.create("/times/" + newReservationTime.getId());
        return ResponseEntity.created(uri).body(newReservationTime);
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> read(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long themeId
            ) {
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.read(date, themeId);
        return ResponseEntity.ok(reservationTimes);
    }

    @PutMapping("/admin/times/{id}")
    public ResponseEntity<Void> update(@RequestBody ReservationTimeUpdateRequest newReservationTimeReq, @PathVariable Long id) {
        reservationTimeService.update(id, newReservationTimeReq);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
