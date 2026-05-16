package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.dto.dto.ReservationRequest;
import roomescape.reservationtime.dto.dto.ReservationResponse;
import roomescape.reservation.service.UserReservationService;

@RestController
@RequestMapping("/reservations")
public class UserReservationController {
    private static final String HEADER_USER_NAME = "X-User-Name";

    private final UserReservationService userReservationService;

    public UserReservationController(UserReservationService userReservationService) {
        this.userReservationService = userReservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<Reservation> reservations = userReservationService.getReservations();
        List<ReservationResponse> response = reservations.stream()
                .map(ReservationResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = userReservationService.createReservation(reservationRequest.name(),
                reservationRequest.date(), reservationRequest.timeId(), reservationRequest.themeId());
        ReservationResponse response = ReservationResponse.from(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(
            @RequestHeader(HEADER_USER_NAME) String name) {
        List<ReservationResponse> response = userReservationService.getMyReservations(decodeName(name)).stream()
                .map(ReservationResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable long id,
                                                  @RequestHeader(HEADER_USER_NAME) String name) {
        userReservationService.deleteReservation(id, decodeName(name));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateReservation(
            @PathVariable long id,
            @RequestHeader(HEADER_USER_NAME) String name,
            @Valid @RequestBody ReservationUpdateRequest request) {
        ReservationResponse response = ReservationResponse.from(
                userReservationService.updateReservation(id, decodeName(name), request.date(), request.timeId()));
        return ResponseEntity.ok(response);
    }

    private static String decodeName(String raw) {
        return URLDecoder.decode(raw, StandardCharsets.UTF_8);
    }
}
