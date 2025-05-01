package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.AvailableTimeResponse;
import roomescape.controller.dto.ReservationTimeRequest;
import roomescape.controller.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> create(
            @RequestBody @Valid final ReservationTimeRequest reservationTimeRequest
    ) {
        ReservationTimeResponse response = reservationTimeService.createReservationTime(reservationTimeRequest);
        return ResponseEntity.created(URI.create("/times/" + response.id())).body(response);
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> findAll() {
        List<ReservationTimeResponse> responses = reservationTimeService.getReservationTimes();
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        reservationTimeService.deleteReservationTimeById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTimeResponse>> findAvailableTimes(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") long themeId
    ) {
        List<AvailableTimeResponse> responses = reservationTimeService.findAvailableTimes(date, themeId);
        return ResponseEntity.ok().body(responses);
    }
}
