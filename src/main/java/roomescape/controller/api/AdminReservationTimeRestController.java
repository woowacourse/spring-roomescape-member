package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.reservationtime.dto.ReservationTimeCreateRequest;
import roomescape.domain.reservationtime.dto.ReservationTimeResponse;
import roomescape.domain.reservationtime.dto.ReservationTimeUpdateRequest;
import roomescape.service.ReservationTimeService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RequestMapping("/admin/times")
@RestController
public class AdminReservationTimeRestController {

    private final ReservationTimeService reservationTimeService;

    public AdminReservationTimeRestController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(@Valid  @RequestBody ReservationTimeCreateRequest reservationTimeReq) {
        ReservationTimeResponse newReservationTime = reservationTimeService.create(reservationTimeReq);
        URI uri = URI.create("/admin/times/" + newReservationTime.getId());
        return ResponseEntity.created(uri).body(newReservationTime);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> read(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long themeId
            ) {
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.read(date, themeId);
        return ResponseEntity.ok(reservationTimes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationTimeResponse> update(@PathVariable Long id, @Valid @RequestBody ReservationTimeUpdateRequest newReservationTimeReq) {
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.update(id, newReservationTimeReq);
        return ResponseEntity.ok(reservationTimeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
