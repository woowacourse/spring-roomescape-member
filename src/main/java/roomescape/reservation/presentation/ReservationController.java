package roomescape.reservation.presentation;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.member.domain.Member;
import roomescape.reservation.application.service.ReservationService;
import roomescape.reservation.presentation.dto.ReservationRequest;
import roomescape.reservation.presentation.dto.ReservationResponse;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            final @RequestBody @Valid ReservationRequest reservationRequest, Member member
    ) {
        ReservationResponse reservation = reservationService.createReservation(reservationRequest, member);

        return ResponseEntity.created(createUri(reservation.getId()))
                .body(reservation);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations(
        final @RequestParam(required = false) Long memberId,
        final @RequestParam(required = false) Long themeId,
        final @RequestParam(required = false) LocalDate dateFrom,
        final @RequestParam(required = false) LocalDate dateTo
    ) {
        return ResponseEntity.ok().body(
                reservationService.getReservations(memberId, themeId, dateFrom, dateTo)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            final @PathVariable Long id
    ) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    private URI createUri(Long reservationId) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservationId)
                .toUri();
    }
}
