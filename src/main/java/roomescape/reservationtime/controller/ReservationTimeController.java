package roomescape.reservationtime.controller;

import java.net.URI;
import java.time.LocalDate;
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
import roomescape.reservationtime.dto.request.ReservationTimeCreateRequest;
import roomescape.reservationtime.dto.response.AvailableReservationTimeResponse;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        return ResponseEntity.ok(reservationTimeService.getReservationTimes());
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableReservationTimeResponse>> getAvailableReservationTimes(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") Long themeId
    ) {
        return ResponseEntity.ok(reservationTimeService.getAvailableReservationTimes(date, themeId));
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createReservationTime(
            @RequestBody ReservationTimeCreateRequest request
    ) {
        return ResponseEntity.created(URI.create("")).body(reservationTimeService.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTimes(
            @PathVariable("id") long id
    ) {
        reservationTimeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
