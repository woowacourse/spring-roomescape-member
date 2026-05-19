package roomescape.controller.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.policy.ReservationCancelPolicy;
import roomescape.policy.ReservationSavePolicy;
import roomescape.policy.UserReservationCancelPolicy;
import roomescape.policy.UserReservationSavePolicy;
import roomescape.request.ReservationEditRequest;
import roomescape.request.ReservationRequest;
import roomescape.response.ReservationResponse;
import roomescape.service.ReservationService;

import java.net.URI;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private static final String DEFAULT_PATH = "/reservations/";
    private final ReservationService reservationService;
    private final Clock clock;
    private final ReservationSavePolicy savePolicy;
    private final ReservationCancelPolicy cancelPolicy;

    public ReservationController(
            ReservationService reservationService,
            Clock clock,
            @Qualifier("userReservationSavePolicy") ReservationSavePolicy savePolicy,
            @Qualifier("userReservationCancelPolicy") ReservationCancelPolicy cancelPolicy) {
        this.reservationService = reservationService;
        this.clock = clock;
        this.savePolicy = savePolicy;
        this.cancelPolicy = cancelPolicy;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations(@RequestParam("name") String name) {
        List<ReservationResponse> responses = ReservationResponse.from(reservationService.findReservationsByName(name));
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> saveReservation(@Valid @RequestBody ReservationRequest request) {
        LocalDateTime now = LocalDateTime.now(clock);
        Reservation reservationReturned = reservationService.saveReservation(request.toSaveCommand(), now, savePolicy);
        ReservationResponse reservationResponse = ReservationResponse.from(reservationReturned);

        return ResponseEntity.created(getLocation(reservationResponse.id())).body(reservationResponse);
    }

    @NonNull
    private static URI getLocation(Long id) {
        return URI.create(DEFAULT_PATH + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.updateCanceled(id, LocalDateTime.now(clock), cancelPolicy);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReservationResponse> editReservation(@PathVariable Long id,
                                                               @Valid @RequestBody ReservationEditRequest request) {
        LocalDateTime now = LocalDateTime.now(clock);
        Reservation reservation = reservationService.editReservation(id, request.toCommand(), now);
        return ResponseEntity.ok(ReservationResponse.from(reservation));
    }
}
