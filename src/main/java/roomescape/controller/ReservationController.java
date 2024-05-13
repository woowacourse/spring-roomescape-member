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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.Member;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createMemberReservation(
            @RequestBody ReservationRequest reservationRequest,
            Member member) {
        Long savedId = reservationService.addReservation(member, reservationRequest);
        ReservationResponse reservationResponse = reservationService.getReservation(savedId);
        return ResponseEntity.created(URI.create("/reservations/" + savedId)).body(reservationResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> reservationResponses = reservationService.getAllReservations();
        return ResponseEntity.ok(reservationResponses);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ReservationResponse>> getFilteredReservations(
            @RequestParam Long themeId,
            @RequestParam Long memberId,
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo
    ) {
        List<ReservationResponse> reservationResponses = reservationService.getFilteredReservations(themeId, memberId,
                dateFrom, dateTo);
        return ResponseEntity.ok(reservationResponses);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable Long id) {
        ReservationResponse reservationResponse = reservationService.getReservation(id);
        return ResponseEntity.ok(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
