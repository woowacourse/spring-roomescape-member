package roomescape.controller.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.policy.AdminReservationCancelPolicy;
import roomescape.policy.AdminReservationSavePolicy;
import roomescape.policy.ReservationCancelPolicy;
import roomescape.policy.ReservationSavePolicy;
import roomescape.request.ReservationRequest;
import roomescape.response.ReservationResponse;
import roomescape.service.ReservationService;

import java.net.URI;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    private static final String DEFAULT_PATH = "/reservations/";
    private final ReservationService reservationService;
    private final Clock clock;
    private final ReservationSavePolicy savePolicy;
    private final ReservationCancelPolicy cancelPolicy;

    public AdminReservationController(
            ReservationService reservationService,
            Clock clock,
            @Qualifier("adminReservationSavePolicy") AdminReservationSavePolicy savePolicy,
            @Qualifier("adminReservationCancelPolicy") AdminReservationCancelPolicy cancelPolicy) {
        this.reservationService = reservationService;
        this.clock = clock;
        this.savePolicy = savePolicy;
        this.cancelPolicy = cancelPolicy;
    }

    @GetMapping
    public List<ReservationResponse> getReservations() {
        return ReservationResponse.from(reservationService.findAllReservations());
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
}
