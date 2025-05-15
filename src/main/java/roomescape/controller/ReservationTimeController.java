package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.search.ReservationTimeResponseWithBookedStatus;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    @Autowired
    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTime> create(@RequestBody ReservationTimeRequest reservationTimeRequest) {
        ReservationTime createdReservationTime = reservationTimeService.saveReservationTime(reservationTimeRequest);
        URI location = URI.create("/times/" + createdReservationTime.getId());
        return ResponseEntity.created(location).body(createdReservationTime);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTime>> readAll() {
        List<ReservationTime> reservationTimes = reservationTimeService.readReservationTime();
        return ResponseEntity.ok(reservationTimes);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReservationTimeResponseWithBookedStatus>> readAll(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long themeId) {
        List<ReservationTimeResponseWithBookedStatus> response = reservationTimeService.readAvailableTimesBy(
                date, themeId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
