package roomescape.reservation.controller;

import static roomescape.auth.LoginUser.SESSION_KEY;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import roomescape.auth.LoginUser;
import roomescape.reservation.controller.dto.ReservationDateTimeChangeRequest;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.ReservationService;

@RequestMapping("/reservations")
@RestController
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/me")
    public List<ReservationResponse> getMyReservations(@SessionAttribute(SESSION_KEY) LoginUser loginUser) {
        return reservationService.getMyReservations(loginUser.name()).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody @Valid ReservationRequest request) {
        final Reservation reservation = reservationService.createReservation(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ReservationResponse.from(reservation));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReservationResponse> changeMyReservationDateTime(
            @PathVariable long id,
            @SessionAttribute(SESSION_KEY) LoginUser loginUser,
            @RequestBody @Valid ReservationDateTimeChangeRequest request
    ) {
        Reservation reservation = reservationService.changeMyReservationDateTime(
                id,
                loginUser.name(),
                request.date(),
                request.timeId());

        return ResponseEntity.ok(ReservationResponse.from(reservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyReservation(
            @PathVariable long id,
            @SessionAttribute(SESSION_KEY) LoginUser loginUser
    ) {
        reservationService.deleteMyReservation(id, loginUser.name());
        return ResponseEntity.noContent().build();
    }
}
