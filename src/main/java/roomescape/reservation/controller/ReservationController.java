package roomescape.reservation.controller;

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
import roomescape.auth.domain.Member;
import roomescape.globar.config.AuthenticationPrincipal;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.SaveReservationRequest;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

  private final ReservationService reservationService;

  public ReservationController(final ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @GetMapping
  public List<ReservationResponse> getReservations(
      @RequestParam(required = false) LocalDate dateFrom,
      @RequestParam(required = false) LocalDate dateTo,
      @RequestParam(required = false) Long themeId,
      @RequestParam(required = false) Long memberId
  ) {
    return reservationService.getReservations(dateFrom, dateTo, themeId, memberId)
        .stream()
        .map(ReservationResponse::from)
        .toList();
  }

  @PostMapping
  public ResponseEntity<ReservationResponse> saveReservation(
      @AuthenticationPrincipal Member member,
      @RequestBody final SaveReservationRequest request
  ) {
    final Reservation savedReservation = reservationService.saveReservation(member, request);

    return ResponseEntity.created(URI.create("/reservations/" + savedReservation.getId()))
        .body(ReservationResponse.from(savedReservation));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReservation(@PathVariable final Long id) {
    reservationService.deleteReservation(id);
    return ResponseEntity.noContent().build();
  }
}
