package roomescape.reservation.ui;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.domain.AuthRole;
import roomescape.auth.domain.RequiresRole;
import roomescape.member.domain.Member;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.ui.dto.AvailableReservationTimeRequest;
import roomescape.reservation.ui.dto.AvailableReservationTimeResponse;
import roomescape.reservation.ui.dto.CreateReservationRequest;
import roomescape.reservation.ui.dto.CreateReservationResponse;
import roomescape.reservation.ui.dto.ReservationResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationRestController {

    private final ReservationService reservationService;

    @PostMapping
    @RequiresRole(authRoles = {AuthRole.ADMIN, AuthRole.MEMBER})
    public ResponseEntity<CreateReservationResponse> create(
            @RequestBody final CreateReservationRequest request,
            final Member member
    ) {
        final CreateReservationResponse response = reservationService.create(request, member);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{id}")
    @RequiresRole(authRoles = {AuthRole.ADMIN, AuthRole.MEMBER})
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        reservationService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @RequiresRole(authRoles = {AuthRole.ADMIN, AuthRole.MEMBER, AuthRole.GUEST})
    public ResponseEntity<List<ReservationResponse>> findAll() {
        final List<ReservationResponse> reservationResponses = reservationService.findAll();

        return ResponseEntity.ok(reservationResponses);
    }

    @GetMapping("/available-times")
    @RequiresRole(authRoles = {AuthRole.ADMIN, AuthRole.MEMBER, AuthRole.GUEST})
    public ResponseEntity<List<AvailableReservationTimeResponse>> findAvailableReservationTimes(
            @ModelAttribute final AvailableReservationTimeRequest request) {
        final List<AvailableReservationTimeResponse> availableReservationTimes
                = reservationService.findAvailableReservationTimes(request);

        return ResponseEntity.ok(availableReservationTimes);
    }
}
