package roomescape.api;

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
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.TimeWithStatusResponse;
import roomescape.facade.ReservationFacade;
import roomescape.service.ReservationTimeService;
import roomescape.utils.DateTimeConverter;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;
    private final ReservationFacade reservationFacade;

    public ReservationTimeController(ReservationTimeService reservationTimeService,
                                     ReservationFacade reservationFacade) {
        this.reservationTimeService = reservationTimeService;
        this.reservationFacade = reservationFacade;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> search() {
        List<ReservationTimeResponse> responses = reservationTimeService.getReservationTimes()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();

        return ResponseEntity.ok().body(responses);
    }

    @GetMapping(params = {"date", "themeId"})
    public ResponseEntity<List<TimeWithStatusResponse>> searchAvailableReservationTime(@RequestParam LocalDate date,
                                                                                       @RequestParam Long themeId) {
        return ResponseEntity.ok().body(reservationFacade.getTimesWithAvailability(date, themeId));
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> add(@RequestBody ReservationTimeRequest request) {
        ReservationTime time = new ReservationTime(null, DateTimeConverter.timeConverter(request.startAt()));
        ReservationTimeResponse response = ReservationTimeResponse.from(reservationTimeService.addTime(time));

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationFacade.deleteTime(id);

        return ResponseEntity.ok().build();
    }
}
