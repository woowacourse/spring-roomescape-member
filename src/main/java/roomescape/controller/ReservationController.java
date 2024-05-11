package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import roomescape.annotation.AuthenticationPrincipal;
import roomescape.controller.request.AdminReservationRequest;
import roomescape.controller.request.ReservationRequest;
import roomescape.controller.response.ReservationResponse;
import roomescape.model.Reservation;
import roomescape.model.User;
import roomescape.service.ReservationService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<Reservation> allReservations = reservationService.findAllReservations();
        List<ReservationResponse> responses = allReservations.stream()
                .map(ReservationResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/admin/reservations")
    public ResponseEntity<List<ReservationResponse>> searchReservations(@RequestParam("themeId") Long themeId,
                                                                        @RequestParam("memberId") Long memberId,
                                                                        @RequestParam("dateFrom") LocalDate dateFrom,
                                                                        @RequestParam("dateTo") LocalDate dateTo) {
        List<Reservation> reservations = reservationService.filterReservation(themeId, memberId, dateFrom, dateTo);
        List<ReservationResponse> responses = reservations.stream()
                .map(ReservationResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<Reservation> createReservation(@RequestBody AdminReservationRequest request) {
        Reservation reservation = reservationService.addReservation(request);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest request,
                                                         @AuthenticationPrincipal User user) {
        Reservation reservation = reservationService.addReservation(request, user);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
