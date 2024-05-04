package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.AvailableReservationTimeResponse;
import roomescape.service.dto.ReservationTimeCreateRequest;
import roomescape.service.dto.ReservationTimeReadRequest;
import roomescape.service.dto.AllReservationTimeResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<AllReservationTimeResponse> createReservationTime(
            @RequestBody @Valid final ReservationTimeCreateRequest reservationTimeCreateRequest) {
        AllReservationTimeResponse allReservationTimeResponse = reservationTimeService.create(reservationTimeCreateRequest);
        return ResponseEntity.created(URI.create("/times/" + allReservationTimeResponse.id()))
                .body(allReservationTimeResponse);
    }

    @GetMapping
    public List<AllReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeService.findAll();
    }

    @GetMapping("/available")
    public List<AvailableReservationTimeResponse> findAvailableReservationTimes(@RequestParam(value = "date") String date,
                                                                                @RequestParam(value = "themeId") long themeId) {
        ReservationTimeReadRequest reservationTimeReadRequest = new ReservationTimeReadRequest(date, themeId);
        return reservationTimeService.findAvailableTimes(reservationTimeReadRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTimeById(@PathVariable final long id) {
        reservationTimeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
