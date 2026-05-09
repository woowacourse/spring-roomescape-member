package roomescape.presentation;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.ReservationTimeService;
import roomescape.domain.ReservationTime;
import roomescape.presentation.dto.ReservationTimeRequest;
import roomescape.presentation.dto.ReservationTimeResponse;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService service;

    public ReservationTimeController(ReservationTimeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> saveTime(
            @RequestBody ReservationTimeRequest request
    ) {
        ReservationTime result = service.saveTime(request.startAt());
        ReservationTimeResponse response = ReservationTimeResponse.from(result);
        return ResponseEntity.created(URI.create("/times/" + response.id()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getTimes() {
        List<ReservationTime> times = service.getTimes();
        List<ReservationTimeResponse> responses = times.stream()
                .map(ReservationTimeResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(
            @PathVariable Long id
    ) {
        service.deleteTime(id);
        return ResponseEntity.noContent().build();
    }
}
