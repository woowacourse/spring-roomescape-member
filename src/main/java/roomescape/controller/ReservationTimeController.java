package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.request.ReservationTimeWithBookStatusRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ReservationTimeWithBookStatusResponse;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findAllTimes() {
        return ResponseEntity.ok(reservationTimeService.findAll());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ReservationTimeWithBookStatusResponse>> findReservationTimesWithBookStatus(
            @RequestParam("date") String date,
            @RequestParam("themeId") Long themeId) {
        ReservationTimeWithBookStatusRequest timeRequest = new ReservationTimeWithBookStatusRequest(date, themeId);
        return ResponseEntity.ok(reservationTimeService.findReservationTimesWithBookStatus(timeRequest));
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> addTime(@RequestBody ReservationTimeRequest reservationTimeRequest) {
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.save(reservationTimeRequest);
        return ResponseEntity.created(URI.create("/admin/time"))
                .body(reservationTimeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        boolean isDeleted = reservationTimeService.deleteById(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
