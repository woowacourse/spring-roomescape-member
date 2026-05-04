package roomescape.domain.reservationtime;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservationtime.dto.CreateTimeRequest;
import roomescape.domain.reservationtime.dto.CreateTimeResponse;
import roomescape.domain.reservationtime.dto.ReservationTimeResponse;

@RestController
@RequiredArgsConstructor
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> getAllReservationTime() {
        List<ReservationTimeResponse> response = reservationTimeService.getAllReservationTime();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/times")
    public ResponseEntity<CreateTimeResponse> createReservationTime(@RequestBody CreateTimeRequest request) {
        request.validate();
        CreateTimeResponse response = reservationTimeService.createReservationTime(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.ok().build();
    }
}
