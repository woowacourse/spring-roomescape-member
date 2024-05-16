package roomescape.admin.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.dto.SaveAdminReservationRequest;
import roomescape.auth.domain.Member;
import roomescape.global.config.AuthenticationPrincipal;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("admin")
public class AdminController {

  private final ReservationService reservationService;

  public AdminController(final ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @PostMapping("/reservations")
  public ResponseEntity<ReservationResponse> saveAdminReservation(
      @AuthenticationPrincipal final Member member,
      @RequestBody final SaveAdminReservationRequest request
  ) {
    final Reservation savedReservation = reservationService.saveAdminReservation(member, request);

    return ResponseEntity.created(URI.create("/reservations/" + savedReservation.getId()))
        .body(ReservationResponse.from(savedReservation));
  }
}
