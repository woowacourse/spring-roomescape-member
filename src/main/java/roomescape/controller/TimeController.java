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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.dto.request.ReservationTimeCreateRequest;
import roomescape.dto.response.ReservationTimeCreateResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ReservationTimeUserResponse;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final ReservationTimeService reservationTimeService;

    public TimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/available")
    public List<ReservationTimeUserResponse> findAllByDateAndTheme(@RequestParam(value = "theme-id") long themeId,
                                                                   @RequestParam(value = "date") LocalDate date) {
        return reservationTimeService.findAllByDateAndTheme(themeId, date);
    }

    @GetMapping
    public List<ReservationTimeResponse> findAll() {
        return reservationTimeService.findAll();
    }

    @PostMapping
    public ResponseEntity<ReservationTimeCreateResponse> create(
            @RequestBody ReservationTimeCreateRequest reservationTimeCreateRequest) {
        ReservationTimeCreateResponse reservationTimeCreateResponse = reservationTimeService.create(
                reservationTimeCreateRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservationTimeCreateResponse.id())
                .toUri();
        return ResponseEntity.created(location).body(reservationTimeCreateResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        reservationTimeService.deleteIfNoReservation(id);
        return ResponseEntity.noContent().build();
    }
}
