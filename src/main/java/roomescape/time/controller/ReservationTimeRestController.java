package roomescape.time.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.time.controller.dto.ReservationTimeRequest;
import roomescape.time.controller.dto.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/times")
public class ReservationTimeRestController {

    private final ReservationTimeService reservationTimeService;

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> persistReservationTime(
            @RequestBody final ReservationTimeRequest request
    ) {
        final Long id = reservationTimeService.save(request.startAt());
        final ReservationTime found = reservationTimeService.getById(id);

        return ResponseEntity.ok(ReservationTimeResponse.from(found));
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> retrieveReservationTimes() {
        return ResponseEntity.ok(
                reservationTimeService.findAll().stream()
                        .map(ReservationTimeResponse::from)
                        .toList()
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteReservationTime(
            @PathVariable final Long id
    ) {
        reservationTimeService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
