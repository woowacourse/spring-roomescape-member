package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Member;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationAdminRequest;
import roomescape.service.dto.ReservationCookieRequest;
import roomescape.service.dto.ReservationResponse;
import roomescape.service.dto.ReservationSpecificRequest;

@RestController
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> findReservations() {
        return reservationService.findAllReservations();
    }

    @GetMapping("/reservations/specific")
    public List<ReservationResponse> findSpecificReservations(
            @RequestParam Long themeId,
            @RequestParam Long memberId,
            @RequestParam String dateFrom,
            @RequestParam String dateTo) {
        ReservationSpecificRequest request = new ReservationSpecificRequest(themeId, memberId, dateFrom, dateTo);
        return reservationService.findSpecificReservations(request);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation2(
            @RequestBody ReservationCookieRequest request, Member member) {
        ReservationResponse reservation = reservationService.createCookieReservation(request, member);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createReservation3(@RequestBody ReservationAdminRequest request) {
        ReservationResponse reservation = reservationService.createAdminReservation(request);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/reservations/{id}")
    public void deleteReservation(@PathVariable long id) {
        reservationService.deleteReservation(id);
    }
}
