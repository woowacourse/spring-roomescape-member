package roomescape.controller;

import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Member;
import roomescape.dto.request.MemberReservationCreationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponse> getReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/search/{themeId}/{memberId}/{dateFrom}/{dateTo}")
    public List<ReservationResponse> searchReservations(
            @PathVariable("themeId") Long themeId,
            @PathVariable("memberId") Long memberId,
            @PathVariable("dateFrom") LocalDate dateFrom,
            @PathVariable("dateTo") LocalDate dateTo
    ) {
        return reservationService.searchReservation(themeId, memberId, dateFrom, dateTo);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody MemberReservationCreationRequest request,
            Member member
    ) {
        long id = reservationService.saveReservationForMember(request, member);
        ReservationResponse savedReservation = reservationService.getReservationById(id);
        return ResponseEntity.created(URI.create("reservations/" + id)).body(savedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
