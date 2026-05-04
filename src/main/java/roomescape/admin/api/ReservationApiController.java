package roomescape.admin.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.service.ReservationService;
import roomescape.service.command.ReservationCommand;
import roomescape.service.result.ReservationResult;

@RestController
@RequestMapping("/admin/reservations")
@Validated
@RequiredArgsConstructor
public class ReservationApiController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResult> reserve(@Valid @RequestBody ReservationCommand request) {
        ReservationResult result = reservationService.reserve(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable
            @Positive(message = "예약 취소 식별자는 양수여야 합니다.")
            Long id
    ) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationResult>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }
}
