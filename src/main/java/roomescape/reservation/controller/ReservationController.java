package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
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
import roomescape.global.annotation.LoginMemberId;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.UserReservationRequest;
import roomescape.reservation.service.ReservationDefaultService;

@RestController
@RequestMapping
public class ReservationController {
    private final ReservationDefaultService reservationDefaultService;

    public ReservationController(ReservationDefaultService reservationDefaultService) {
        this.reservationDefaultService = reservationDefaultService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations(
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) Long themeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok().body(reservationDefaultService.getFiltered(memberId, themeId, from, to));
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservationForUser(
            @Valid @RequestBody UserReservationRequest request,
            @LoginMemberId Long memberId) {
        ReservationResponse response = reservationDefaultService.createForUser(request, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createReservationForAdmin(
            @Valid @RequestBody AdminReservationRequest request) {
        ReservationResponse response = reservationDefaultService.createForAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationDefaultService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
