package roomescape.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.SearchRequest;
import roomescape.reservation.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAll() {
        List<Reservation> reservations = reservationService.readAll();

        List<ReservationResponse> reservationResponses = changeToReservationResponses(reservations);

        return ResponseEntity.ok(reservationResponses);
    }

    @PostMapping("/search")
    public ResponseEntity<List<ReservationResponse>> findBySearchInfo(@RequestBody SearchRequest searchRequest) {
        List<Reservation> reservations = reservationService.readBySearchInfo(searchRequest.toSearchInfo());

        List<ReservationResponse> reservationResponses = changeToReservationResponses(reservations);

        return ResponseEntity.ok(reservationResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> save(@RequestBody ReservationRequest reservationRequest, Member member) {
        Reservation reservation = reservationService.create(reservationRequest.toReservation(member));

        ReservationResponse reservationResponse = changeToReservationResponse(reservation);
        String url = "/reservations/" + reservationResponse.id();

        return ResponseEntity.created(URI.create(url)).body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        reservationService.delete(id);

        return ResponseEntity.noContent().build();
    }

    private ReservationResponse changeToReservationResponse(Reservation reservation) {
        return new ReservationResponse(reservation);
    }

    private List<ReservationResponse> changeToReservationResponses(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::changeToReservationResponse)
                .toList();
    }
}
