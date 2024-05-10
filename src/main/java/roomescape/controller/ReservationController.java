package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import roomescape.service.ReservationService;
import roomescape.service.dto.request.LoginUser;
import roomescape.service.dto.request.ReservationRequest;
import roomescape.service.dto.response.ReservationResponse;


@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> postReservation(@RequestBody ReservationRequest reservationRequest,
                                                               LoginUser loginUser) {
        ReservationResponse reservationResponse = reservationService.createReservation(reservationRequest, loginUser);
        URI location = UriComponentsBuilder.newInstance()
                .path("/reservations/{id}")
                .buildAndExpand(reservationResponse.id())
                .toUri();

        return ResponseEntity.created(location)
                .body(reservationResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservationsOf() {
        return ResponseEntity.ok(reservationService.findAllReservations());
    }

    @GetMapping(params = {"themeId", "memberId", "dateFrom", "dateTo"})
    public ResponseEntity<List<ReservationResponse>> getReservationsOf(@RequestParam Long themeId,
                                                                       @RequestParam Long memberId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                     LocalDate dateFrom,
                                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                     LocalDate dateTo
    ) {
        return ResponseEntity.ok(reservationService.findAllReservationsOf(themeId, memberId, dateFrom, dateTo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent()
                .build();
    }
}
