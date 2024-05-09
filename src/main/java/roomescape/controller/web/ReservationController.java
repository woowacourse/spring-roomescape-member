package roomescape.controller.web;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.MemberRequest;
import roomescape.dto.request.UserReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.SelectableTimeResponse;
import roomescape.service.ReservationService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> saveReservation(
            @RequestBody @Valid UserReservationRequest userReservationRequest,
            @Valid MemberRequest memberRequest) {
        ReservationResponse reservationResponse = reservationService.save(userReservationRequest, memberRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @GetMapping("/times")
    public ResponseEntity<List<SelectableTimeResponse>> findSelectableTimes(
            @RequestParam(name = "date") LocalDate date,
            @RequestParam(name = "themeId") long themeId
    ) {
        return ResponseEntity.ok(reservationService.findSelectableTime(date, themeId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
