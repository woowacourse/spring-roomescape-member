package roomescape.reservation.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.service.dto.AvailableTimeInfo;
import roomescape.reservation.service.dto.CreateReservationTimeCommand;
import roomescape.reservation.service.dto.ReservationTimeInfo;
import roomescape.reservation.service.ReservationTimeService;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeInfo> create(
            @RequestBody @Valid final CreateReservationTimeCommand createReservationTimeCommand
    ) {
        ReservationTimeInfo response = reservationTimeService.createReservationTime(createReservationTimeCommand);
        return ResponseEntity.created(URI.create("/times/" + response.id())).body(response);
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeInfo>> findAll() {
        List<ReservationTimeInfo> responses = reservationTimeService.getReservationTimes();
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        reservationTimeService.deleteReservationTimeById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTimeInfo>> findAvailableTimes(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") long themeId
    ) {
        List<AvailableTimeInfo> responses = reservationTimeService.findAvailableTimes(date, themeId);
        return ResponseEntity.ok().body(responses);
    }
}
