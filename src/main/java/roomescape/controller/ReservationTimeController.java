package roomescape.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.context.annotation.Profile;
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
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.ReservationTime.ReservationTimeCommand;
import roomescape.domain.ReservationTime.ReservationTimeCondition;
import roomescape.domain.ReservationTime.ReservationTimeWithAvailable;
import roomescape.dto.ReservationTime.AddReservationTimeRequest;
import roomescape.dto.ReservationTime.AvailableReservationTimeResponse;
import roomescape.dto.ReservationTime.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RestController
@Profile("web")
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

        return new ResponseEntity<>(reservationTimeResponses, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ReservationTimeResponse> addReservationTime(@RequestBody @Valid AddReservationTimeRequest addReservationTimeRequest) {
        ReservationTimeCommand reservationTimeCommand = new ReservationTimeCommand(addReservationTimeRequest.startAt());
        ReservationTime reservationTime = reservationTimeService.addReservationTime(reservationTimeCommand);

        return new ResponseEntity<>(ReservationTimeResponse.from(reservationTime), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable("id") long id) {
        reservationTimeService.deleteReservationTime(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/availability", params = {"date", "themeId"})
    public ResponseEntity<List<AvailableReservationTimeResponse>> getAvailableReservationTimeByDateAndTheme(@ModelAttribute ReservationTimeCondition reservationTimeCondition) {
        ReservationTimeCondition reservationTimeWithAvailableCondition = new ReservationTimeCondition(reservationTimeCondition.date(), reservationTimeCondition.themeId());
        List<ReservationTimeWithAvailable> reservationTimesWithAvailable  = reservationTimeService.getAvailableReservationTimeByDateAndTheme(reservationTimeWithAvailableCondition);
        List<AvailableReservationTimeResponse> availableReservationTimeResponses = reservationTimesWithAvailable.stream()
                .map(AvailableReservationTimeResponse::from)
                .toList();

        return new ResponseEntity<>(availableReservationTimeResponses, HttpStatus.OK);
    }
}
