package roomescape.domain.reservation.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.login.controller.AuthenticationPrincipal;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.dto.BookableTimeResponse;
import roomescape.domain.reservation.dto.BookableTimesRequest;
import roomescape.domain.reservation.dto.ReservationAddRequest;
import roomescape.domain.reservation.service.AdminReservationService;
import roomescape.domain.reservation.service.ReservationService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final AdminReservationService adminReservationService;

    public ReservationController(ReservationService reservationService,
                                 AdminReservationService adminReservationService) {
        this.reservationService = reservationService;
        this.adminReservationService = adminReservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> addReservation(@RequestBody ReservationAddRequest reservationAddRequest,
                                                      @AuthenticationPrincipal Member member) {
        reservationAddRequest = new ReservationAddRequest(reservationAddRequest.date(), reservationAddRequest.timeId(),
                reservationAddRequest.themeId(), member.getId());
        Reservation reservation = adminReservationService.addReservation(reservationAddRequest);
        return ResponseEntity.created(URI.create("/reservation/" + reservation.getId())).body(reservation);
    }

    @GetMapping("/bookable-times")
    public ResponseEntity<List<BookableTimeResponse>> getTimesWithStatus(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") Long themeId) {
        return ResponseEntity.ok(reservationService.findBookableTimes(new BookableTimesRequest(date, themeId)));
    }
}
