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
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationTimeAddRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ReservationTimeWithBookStatusResponse;
import roomescape.service.ReservationTimeService;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimeList() {
        return ResponseEntity.ok(reservationTimeService.findAllReservationTime());
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> saveReservationTime(
            @RequestBody ReservationTimeAddRequest reservationTimeAddRequest) {
        ReservationTimeResponse saveResponse = reservationTimeService.saveReservationTime(reservationTimeAddRequest);
        return ResponseEntity.created(URI.create("/times/" + saveResponse.id())).body(saveResponse);
    }

    @GetMapping("/times/{date}/{themeId}")
    public ResponseEntity<List<ReservationTimeWithBookStatusResponse>> readTimesStatus(
            @PathVariable("date") LocalDate date,
            @PathVariable("themeId") Long themeId) {
        return ResponseEntity.ok(reservationTimeService.findAllWithBookStatus(date, themeId));
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> removeReservationTime(@PathVariable("id") Long id) {
        reservationTimeService.removeReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
