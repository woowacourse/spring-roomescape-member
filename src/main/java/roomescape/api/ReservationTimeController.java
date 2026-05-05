package roomescape.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.facade.ReservationTimeFacade;
import roomescape.service.ReservationTimeService;
import roomescape.utils.DateTimeConverter;

import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;
    private final ReservationTimeFacade reservationTimeFacade;

    public ReservationTimeController(ReservationTimeService reservationTimeService, ReservationTimeFacade reservationTimeFacade) {
        this.reservationTimeService = reservationTimeService;
        this.reservationTimeFacade = reservationTimeFacade;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> search() {
        List<ReservationTimeResponse> responses = reservationTimeService.getTimes()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();

        return ResponseEntity.ok().body(responses);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> add(@RequestBody ReservationTimeRequest request) {
        ReservationTime time = new ReservationTime(null, DateTimeConverter.timeConverter(request.startAt()));
        ReservationTimeResponse response = ReservationTimeResponse.from(reservationTimeService.addTime(time));

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeFacade.deleteTime(id);

        return ResponseEntity.ok().build();
    }
}
