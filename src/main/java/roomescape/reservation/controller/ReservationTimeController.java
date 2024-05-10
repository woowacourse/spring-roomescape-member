package roomescape.reservation.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.service.ReservationTimeService;
import roomescape.reservation.service.dto.AvailableReservationTimeResponse;
import roomescape.reservation.service.dto.ReservationTimeCreateRequest;
import roomescape.reservation.service.dto.ReservationTimeReadRequest;
import roomescape.reservation.service.dto.ReservationTimeResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createReservationTime(@RequestBody @Valid ReservationTimeCreateRequest reservationTimeCreateRequest) {
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(reservationTimeCreateRequest);
        return ResponseEntity.created(URI.create("/times/" + reservationTimeResponse.id()))
                .body(reservationTimeResponse);
    }

    @GetMapping
    public List<ReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeService.findAll();
    }

    @GetMapping("/available")
    public List<AvailableReservationTimeResponse> findAvailableReservationTimes(@RequestParam(value = "date") String date,
                                                                                @RequestParam(value = "themeId") long themeId) {
        ReservationTimeReadRequest reservationTimeReadRequest = new ReservationTimeReadRequest(date, themeId);
        return reservationTimeService.findAvailableTimes(reservationTimeReadRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTimeById(@PathVariable("id") long id) {
        reservationTimeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
