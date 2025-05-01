package roomescape.presentation.api;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.ReservationService;
import roomescape.presentation.dto.request.ReservationRequest;
import roomescape.presentation.dto.response.AvailableReservationTimeResponse;
import roomescape.presentation.dto.response.ReservationResponse;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.PastReservationException;
import roomescape.exception.ReservationTimeConflictException;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> add(@Valid @RequestBody ReservationRequest requestDto) {
        return new ResponseEntity<>(reservationService.add(requestDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/themes/{themeId}/times")
    public ResponseEntity<List<AvailableReservationTimeResponse>> findAvailableReservationTime(
            @PathVariable Long themeId,
            @RequestParam String date) {
        return ResponseEntity.ok(reservationService.findAvailableReservationTime(themeId, date));
    }
}
