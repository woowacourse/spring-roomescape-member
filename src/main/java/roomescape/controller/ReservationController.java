package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Member;
import roomescape.service.ReservationService;
import roomescape.service.dto.reservation.MemberReservationCreateRequest;
import roomescape.service.dto.reservation.ReservationCreateRequest;
import roomescape.service.dto.reservation.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponse> readReservations() {
        return reservationService.readReservations();
    }

    @GetMapping(params = {"dateFrom", "dateTo", "memberId", "themeId"})
    public List<ReservationResponse> readReservations(
            @RequestParam LocalDate dateFrom,
            @RequestParam LocalDate dateTo,
            @RequestParam Long memberId,
            @RequestParam Long themeId
    ) {
        return reservationService.readReservations(dateFrom, dateTo, memberId, themeId);
    }

    @GetMapping("/{id}")
    public ReservationResponse readReservation(@PathVariable Long id) {
        return reservationService.readReservation(id);
    }

    @PostMapping
    public ReservationResponse createReservation(@Valid @RequestBody ReservationCreateRequest request) {
        return reservationService.createReservation(request);
    }

    @PostMapping(params = {"type=member"})
    public ReservationResponse createReservation(
            @Valid @RequestBody MemberReservationCreateRequest request,
            Member member
    ) {
        return reservationService.createReservation(request, member);
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }
}
