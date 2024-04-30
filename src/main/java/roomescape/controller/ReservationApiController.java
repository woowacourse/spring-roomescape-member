package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.SaveReservationRequest;
import roomescape.dto.SaveReservationTimeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationApiController {
    private final ReservationService reservationService;

    public ReservationApiController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> getReservations() {
        return reservationService.getReservations()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> saveReservation(@RequestBody final SaveReservationRequest request) {
        Reservation savedReservation = reservationService.saveReservation(request);

        return ResponseEntity.created(URI.create("/reservations/" + savedReservation.getId()))
                .body(ReservationResponse.from(savedReservation));
    }

    @DeleteMapping("/reservations/{reservation-id}")
    public void deleteReservation(@PathVariable("reservation-id") final Long reservationId) {
        reservationService.deleteReservation(reservationId);
    }

    @GetMapping("/times")
    public List<ReservationTimeResponse> getReservationTimes() {
        return reservationService.getReservationTimes()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> saveReservationTime(@RequestBody final SaveReservationTimeRequest request) {
        ReservationTime savedReservationTime = reservationService.saveReservationTime(request);

        return ResponseEntity.created(URI.create("/times/" + savedReservationTime.getId()))
                .body(ReservationTimeResponse.from(savedReservationTime));
    }

    @DeleteMapping("/times/{reservation-time-id}")
    public void deleteReservationTime(@PathVariable("reservation-time-id") final Long reservationTimeId) {
        reservationService.deleteReservationTime(reservationTimeId);
    }

    @GetMapping("/themes")
    public List<ThemeResponse> getThemes() {
        return reservationService.getThemes()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }
}
