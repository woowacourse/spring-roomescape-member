package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.AvailabilityOfTimeRequest;
import roomescape.service.dto.AvailabilityOfTimeResponse;
import roomescape.service.dto.ReservationTimeRequest;
import roomescape.service.dto.ReservationTimeResponse;

@RestController
public class ReservationTimeApiController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeApiController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public List<ReservationTimeResponse> findReservationTimes() {
        return reservationTimeService.findAllReservationTimes();
    }

    @GetMapping("/times/availability")
    public List<AvailabilityOfTimeResponse> findReservationTimesAvailability(@RequestParam String date,
                                                                             @RequestParam Long themeId) {
        return reservationTimeService.findReservationTimesAvailability(new AvailabilityOfTimeRequest(date, themeId));
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> createReservationTime(
            @RequestBody ReservationTimeRequest requestDto) {
        ReservationTimeResponse reservationTime = reservationTimeService.createReservationTime(requestDto);
        return ResponseEntity.created(URI.create("/times/" + reservationTime.getId())).body(reservationTime);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/times/{id}")
    public void deleteReservationTime(@PathVariable long id) {
        reservationTimeService.deleteReservationTime(id);
    }
}
