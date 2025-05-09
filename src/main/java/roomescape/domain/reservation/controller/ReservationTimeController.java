package roomescape.domain.reservation.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservation.dto.ReservationTimeRequest;
import roomescape.domain.reservation.dto.ReservationTimeResponse;
import roomescape.domain.reservation.service.ReservationTimeService;

@RequestMapping("/times")
@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> readAllReservationTimes() {
        final List<ReservationTimeResponse> responses = reservationTimeService.getAll();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(@Valid @RequestBody final ReservationTimeRequest request) {

        final ReservationTimeResponse response = reservationTimeService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
        reservationTimeService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
