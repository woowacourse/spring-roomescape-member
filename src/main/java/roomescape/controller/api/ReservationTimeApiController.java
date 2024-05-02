package roomescape.controller.api;

import java.net.URI;
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
import roomescape.controller.api.dto.request.ReservationTimeRequest;
import roomescape.controller.api.dto.response.ReservationTimeResponse;
import roomescape.dao.dto.AvailableReservationTimeResponse;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.input.AvailableReservationTimeInput;
import roomescape.service.dto.output.ReservationTimeOutput;

@RestController
@RequestMapping("/times")
public class ReservationTimeApiController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeApiController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createReservationTime(@RequestBody ReservationTimeRequest request) {
        ReservationTimeOutput output = reservationTimeService.createReservationTime(request.toInput());
        return ResponseEntity.created(URI.create("/times/" + output.id()))
                .body(ReservationTimeResponse.toResponse(output));
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getAllReservationTimes() {
        List<ReservationTimeOutput> output = reservationTimeService.getAllReservationTimes();
        return ResponseEntity.ok(ReservationTimeResponse.toResponses(output));
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableReservationTimeResponse>> getAllReservationTimes(
            @RequestParam String date,
            @RequestParam Long themeId) {
        List<AvailableReservationTimeResponse> response = reservationTimeService.getAvailableTimes(
                new AvailableReservationTimeInput(themeId, date));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
