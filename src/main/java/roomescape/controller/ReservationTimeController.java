package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
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
import roomescape.dto.request.ReservationTimesWithTotalPageResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<ReservationTimesWithTotalPageResponse> getReservationTimesByPage(
            @RequestParam(required = false, defaultValue = "1") int page) {
        ReservationTimesWithTotalPageResponse reservationTimesByPage = reservationTimeService.getReservationTimesByPage(
                page);
        return ResponseEntity.ok(reservationTimesByPage);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> addTime(
            @RequestBody @Valid ReservationTimeRequest reservationTimeRequest) {
        ReservationTimeResponse newReservation = reservationTimeService.addTime(reservationTimeRequest);
        Long id = newReservation.id();
        return ResponseEntity.created(URI.create("/time/" + id)).body(newReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable(name = "id") Long id) {
        reservationTimeService.deleteTime(id);
        return ResponseEntity.noContent().build();
    }
}
