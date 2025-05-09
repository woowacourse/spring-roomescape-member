package roomescape.reservation.ui;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.domain.AuthRole;
import roomescape.auth.domain.RequiresRole;
import roomescape.member.domain.Member;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.ui.dto.AvailableReservationTimeRequest;
import roomescape.reservation.ui.dto.AvailableReservationTimeResponse;
import roomescape.reservation.ui.dto.CreateReservationRequest;
import roomescape.reservation.ui.dto.ReservationResponse;
import roomescape.reservation.ui.dto.ReservationsByCriteriaRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationRestController {

    private final ReservationService reservationService;

    @PostMapping("/reservations")
    @RequiresRole(authRoles = {AuthRole.ADMIN, AuthRole.MEMBER})
    public ResponseEntity<ReservationResponse> create(
            @RequestBody final CreateReservationRequest request,
            final Member member
    ) {
        final ReservationResponse response = reservationService.create(request, member);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/reservations/{id}")
    @RequiresRole(authRoles = {AuthRole.ADMIN, AuthRole.MEMBER})
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        reservationService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations")
    @RequiresRole(authRoles = {AuthRole.ADMIN})
    public ResponseEntity<List<ReservationResponse>> findAll() {
        final List<ReservationResponse> reservationResponses = reservationService.findAll();

        return ResponseEntity.ok(reservationResponses);
    }

    @GetMapping("/admin/reservations")
    @RequiresRole(authRoles = {AuthRole.ADMIN})
    public ResponseEntity<List<ReservationResponse>> findAllByCriteria(
            @ModelAttribute final ReservationsByCriteriaRequest request
    ) {
        log.info(request.toString());
        final List<ReservationResponse> reservationResponses = reservationService.findAllByCriteria(request);

        return ResponseEntity.ok(reservationResponses);
    }

    @GetMapping("/reservations/available-times")
    public ResponseEntity<List<AvailableReservationTimeResponse>> findAvailableReservationTimes(
            @ModelAttribute final AvailableReservationTimeRequest request) {
        final List<AvailableReservationTimeResponse> availableReservationTimes
                = reservationService.findAvailableReservationTimes(request);

        return ResponseEntity.ok(availableReservationTimes);
    }
}
