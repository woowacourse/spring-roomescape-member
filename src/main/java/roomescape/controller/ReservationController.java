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
import roomescape.dto.request.ReservationSaveRequest;
import roomescape.dto.response.ReservationDeleteResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.SelectableTimeResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> save(@RequestBody ReservationSaveRequest reservationSaveRequest) {
        ReservationResponse reservationResponse = reservationService.save(reservationSaveRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAll() {
        return ResponseEntity.ok(reservationService.getAll());
    }

    @GetMapping("/times")
    public ResponseEntity<List<SelectableTimeResponse>> findSelectableTimes(
            @RequestParam(name = "date") LocalDate date,
            @RequestParam(name = "themeId") long themeId
    ) {
        return ResponseEntity.ok(reservationService.findSelectableTimes(date, themeId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationDeleteResponse> delete(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(reservationService.delete(id));
    }
}
