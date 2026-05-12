package roomescape.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeCommand;
import roomescape.domain.reservationTime.ReservationTimeCondition;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;
import roomescape.dto.Response;
import roomescape.dto.reservationTime.AddReservationTimeRequest;
import roomescape.dto.reservationTime.AvailableReservationTimeRequest;
import roomescape.dto.reservationTime.AvailableReservationTimeResponse;
import roomescape.dto.reservationTime.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<Response> getReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.getAllReservationTime();
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();

        return ResponseEntity.ok(Response.from(HttpStatus.OK.value(), reservationTimeResponses));
    }

    @PostMapping
    public ResponseEntity<Response> addReservationTime(@RequestBody @Valid AddReservationTimeRequest addReservationTimeRequest) {
        ReservationTimeCommand reservationTimeCommand = new ReservationTimeCommand(addReservationTimeRequest.startAt());
        ReservationTime reservationTime = reservationTimeService.addReservationTime(reservationTimeCommand);

        return new ResponseEntity<>(Response.from(HttpStatus.CREATED.value(), ReservationTimeResponse.from(reservationTime)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("id") long id) {
        reservationTimeService.deleteReservationTime(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/availability", params = {"date", "themeId"})
    public ResponseEntity<Response> getAvailableReservationTimeByDateAndTheme(@ModelAttribute @Valid AvailableReservationTimeRequest availableReservationTimeRequest) {
        ReservationTimeCondition reservationTimeCondition = new ReservationTimeCondition(availableReservationTimeRequest.date(), availableReservationTimeRequest.themeId());
        List<ReservationTimeWithAvailable> reservationTimesWithAvailable  = reservationTimeService.getAvailableReservationTimeByDateAndTheme(reservationTimeCondition);
        List<AvailableReservationTimeResponse> availableReservationTimeResponses = reservationTimesWithAvailable.stream()
                .map(AvailableReservationTimeResponse::from)
                .toList();

        return ResponseEntity.ok(Response.from(HttpStatus.OK.value(), availableReservationTimeResponses));
    }
}
