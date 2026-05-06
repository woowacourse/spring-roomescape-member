package roomescape.reservationtime.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.payload.ReservationTimeRequest;
import roomescape.reservationtime.payload.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;

@Controller
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> postTimes(@Valid @RequestBody ReservationTimeRequest request) {
        ReservationTime reservationTime = reservationTimeService.save(request);
        return ResponseEntity.ok().body(ReservationTimeResponse.from(reservationTime));
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> getAllTimes() {
        List<ReservationTimeResponse> responses = reservationTimeService.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();

        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> getAvailableReservationTimes(@RequestParam LocalDate date,
                                                                                      @RequestParam Long themeId) {
        List<ReservationTimeResponse> availableReservationTimes =
                reservationTimeService.findAvailableReservationTimes(date, themeId).stream()
                        .map(ReservationTimeResponse::from)
                        .toList();
        return ResponseEntity.ok().body(availableReservationTimes);
    }


    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteTimes(@PathVariable Long id) {
        reservationTimeService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
