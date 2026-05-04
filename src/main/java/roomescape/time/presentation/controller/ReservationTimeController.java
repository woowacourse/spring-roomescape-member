package roomescape.time.presentation.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.application.ReservationTimeService;
import roomescape.time.presentation.dto.AvailableReservationTimeRequest;
import roomescape.time.presentation.dto.AvailableReservationTimeResponse;
import roomescape.time.presentation.dto.ReservationTimeRequest;
import roomescape.time.presentation.dto.ReservationTimeResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService service;

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        return ResponseEntity.ok().body(service.getReservationTimes());
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createReservationTime(@Valid @RequestBody ReservationTimeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addReservationTime(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        service.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<AvailableReservationTimeResponse> getAvailableReservationTime(@RequestParam AvailableReservationTimeRequest request) {
        return ResponseEntity.ok(service.getAvailableReservationTime(request));
    }
}
