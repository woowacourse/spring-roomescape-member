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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.application.ReservationTimeService;
import roomescape.entity.ReservationTime;
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
    public ResponseEntity<ReservationTimeResponse> createTime(
            @RequestBody ReservationTimeRequest request
    ) {
        ReservationTime result = service.save(request.startAt());
        ReservationTimeResponse response = ReservationTimeResponse.from(result);
        return ResponseEntity.created(parseCreatedResourceURI(response))
                .body(response);
    }

    private URI parseCreatedResourceURI(ReservationTimeResponse result) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> readTimes() {
        List<ReservationTime> times = service.findAll();
        List<ReservationTimeResponse> responses = convertToReservationResponse(times);
        return ResponseEntity.ok(responses);
    }

    private List<ReservationTimeResponse> convertToReservationResponse(List<ReservationTime> times) {
        return times.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(
            @PathVariable Long id
    ) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
