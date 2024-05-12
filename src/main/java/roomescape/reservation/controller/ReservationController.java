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

    @PostMapping
    public ResponseEntity<ReservationResponse> saveReservation(@RequestBody ReservationRequest reservationRequest, Member member) {
        Reservation reservation = reservationService.saveReservation(reservationRequest.toReservation(member));

        ReservationResponse reservationResponse = changeToReservationResponse(reservation);
        String url = "/reservations/" + reservationResponse.id();

        return ResponseEntity.created(URI.create(url)).body(reservationResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findReservationList() {
        List<Reservation> reservations = reservationService.findReservationList();

        List<ReservationResponse> reservationResponses = changeToReservationResponses(reservations);

        return ResponseEntity.ok(reservationResponses);
    }

    @PostMapping("/search")
    public ResponseEntity<List<ReservationResponse>> findReservationListBySearchInfo(@RequestBody SearchRequest searchRequest) {
        List<Reservation> reservations = reservationService.findReservationListBySearchInfo(searchRequest.toSearchInfo());

        List<ReservationResponse> reservationResponses = changeToReservationResponses(reservations);

        return ResponseEntity.ok(reservationResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationById(@PathVariable("id") long id) {
        reservationService.deleteReservationById(id);

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
