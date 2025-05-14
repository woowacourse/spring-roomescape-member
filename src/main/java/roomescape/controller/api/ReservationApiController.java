package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.reservation.ReservationAdminRequest;
import roomescape.controller.dto.reservation.ReservationMemberRequest;
import roomescape.controller.dto.reservation.ReservationResponse;
import roomescape.entity.Member;
import roomescape.entity.Reservation;
import roomescape.service.ReservationService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createMemberReservation(
            @RequestBody @Valid ReservationMemberRequest request,
            Member member
    ) {
        Reservation reservation = reservationService.createMemberReservation(
                member,
                request.date(),
                request.timeId(),
                request.themeId()
        );
        ReservationResponse response = ReservationResponse.from(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createAdminReservation(@RequestBody @Valid ReservationAdminRequest request) {
        Reservation reservation = reservationService.createAdminReservation(
                request.memberId(),
                request.date(),
                request.timeId(),
                request.themeId()
        );
        ReservationResponse response = ReservationResponse.from(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<Reservation> reservations = reservationService.getReservations();
        List<ReservationResponse> responses = ReservationResponse.from(reservations);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/admin/reservation/search")
    public ResponseEntity<List<ReservationResponse>> getSearchReservations(
            @RequestParam(name = "memberId") Long memberId,
            @RequestParam(name = "themeId") Long themeId,
            @RequestParam(name = "dateFrom") LocalDate dateFrom,
            @RequestParam(name = "dateTo") LocalDate dateTo
    ) {
        List<Reservation> reservations = reservationService.searchReservationsByDateRange(
                memberId,
                themeId,
                dateFrom,
                dateTo
        );

        List<ReservationResponse> responses = ReservationResponse.from(reservations);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
