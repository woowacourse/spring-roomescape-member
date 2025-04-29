package roomescape.time.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.controller.request.ReservationTimeCreateRequest;
import roomescape.time.controller.response.ReservationTimeResponse;
import roomescape.time.service.ReservationTimeServiceImpl;

@RestController
public class ReservationTimeApiController {

    private final ReservationTimeServiceImpl reservationTimeService;

    public ReservationTimeApiController(ReservationTimeServiceImpl reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping("/times")
    public ResponseEntity<ReservationTimeResponse> createTime(
            @RequestBody ReservationTimeCreateRequest request
    ) {
        ReservationTimeResponse response = reservationTimeService.create(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> getTimes() {
        List<ReservationTimeResponse> responses = reservationTimeService.getAll();

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        reservationTimeService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
