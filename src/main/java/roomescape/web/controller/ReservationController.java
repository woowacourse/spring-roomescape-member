package roomescape.web.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.core.dto.ReservationRequestDto;
import roomescape.core.dto.ReservationResponseDto;
import roomescape.core.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> create(@RequestBody final ReservationRequestDto request) {
        validateRequest(request);
        final ReservationResponseDto result = reservationService.create(request);
        return ResponseEntity.created(URI.create("/reservations/" + result.getId()))
                .body(result);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    public void validateRequest(final ReservationRequestDto request) {
        final String date = request.getDate();
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("Date cannot be null or empty");
        }

        final String name = request.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        if (request.getTimeId() == null) {
            throw new IllegalArgumentException("Time ID cannot be null");
        }
    }
}
