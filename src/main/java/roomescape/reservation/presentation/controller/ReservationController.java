package roomescape.reservation.presentation.controller;

import jakarta.validation.Valid;
import java.time.Clock;
import java.time.LocalDateTime;
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
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.reservation.application.dto.ReservationResult;
import roomescape.reservation.application.service.ReservationCommandService;
import roomescape.reservation.application.service.ReservationQueryService;
import roomescape.reservation.presentation.dto.ReservationCreateRequest;
import roomescape.reservation.presentation.dto.ReservationResponse;

@RequiredArgsConstructor
@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private final Clock clock;
    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAll(
            @RequestParam(required = false) String username
    ) {
        List<ReservationResult> results = (username == null)
                ? reservationQueryService.findAll()
                : reservationQueryService.findByName(username);

        List<ReservationResponse> responses = results.stream()
                .map(ReservationResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> save(
            @Valid @RequestBody ReservationCreateRequest request
    ) {
        ReservationCreateCommand createCommand = request.toCommand();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ReservationResponse.from(reservationCommandService.save(createCommand, LocalDateTime.now(clock))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        reservationCommandService.delete(id, LocalDateTime.now(clock));
        return ResponseEntity.noContent().build();
    }
}
