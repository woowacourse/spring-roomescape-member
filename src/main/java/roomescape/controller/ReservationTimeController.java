package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeCondition;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;
import roomescape.dto.reservationTime.AddReservationTimeRequest;
import roomescape.dto.reservationTime.AvailableReservationTimeResponse;
import roomescape.dto.reservationTime.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping()
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.getAllReservationTime();
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();

        return ResponseEntity.ok(reservationTimeResponses);
    }

    @PostMapping()
    public ResponseEntity<ReservationTimeResponse> addReservationTime(@RequestBody @Valid AddReservationTimeRequest addReservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeService.addReservationTime(addReservationTimeRequest);

        return new ResponseEntity(ReservationTimeResponse.from(reservationTime), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("id") long id) {
        reservationTimeService.deleteReservationTime(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/availability", params = {"date", "themeId"})
    public ResponseEntity<List<AvailableReservationTimeResponse>> getAvailableReservationTimeByDateAndTheme(@ModelAttribute @Valid ReservationTimeCondition reservationTimeCondition) {
        List<ReservationTimeWithAvailable> reservationTimesWithAvailable  = reservationTimeService.getAvailableReservationTimeByDateAndTheme(reservationTimeCondition);
        List<AvailableReservationTimeResponse> availableReservationTimeResponses = reservationTimesWithAvailable.stream()
                .map(AvailableReservationTimeResponse::from)
                .toList();

        return ResponseEntity.ok(availableReservationTimeResponses);
    }
}
