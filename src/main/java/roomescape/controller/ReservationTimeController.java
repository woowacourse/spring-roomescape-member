package roomescape.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeCreateRequest;
import roomescape.dto.ReservationTimeListResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(@RequestBody ReservationTimeCreateRequest request) {
        ReservationTime reservationTime = reservationTimeService.create(request.startAt());

        return ResponseEntity.status(CREATED)
                .body(ReservationTimeResponse.from(reservationTime));
    }

    @GetMapping
    public ResponseEntity<ReservationTimeListResponse> list() {
        return ResponseEntity.ok(ReservationTimeListResponse.from(
                reservationTimeService.findAll()
                        .stream()
                        .map(ReservationTimeResponse::from)
                        .toList()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
