package roomescape.presentation;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping(value = "/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findAll() {
        List<ReservationTimeResponse> all = reservationTimeService.findAll();
        return ResponseEntity.ok(all);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(
            @Valid @RequestBody ReservationTimeRequest reservationTimeRequest) {
        ReservationTimeResponse reservationTime = reservationTimeService.create(reservationTimeRequest);
        return ResponseEntity
                .created(URI.create("/times/" + reservationTime.id()))
                .body(reservationTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("theme/{themeId}")
    public ResponseEntity<List<AvailableTimeResponse>> getTimesWithBooked(
            @PathVariable("themeId") Long themeId, @RequestParam("date") LocalDate date
    ) {
        return ResponseEntity.ok(reservationTimeService.findTimesByDateAndThemeIdWithBooked(date, themeId));
    }
}
