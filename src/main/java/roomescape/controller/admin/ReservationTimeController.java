package roomescape.controller.admin;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeCreationRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RestController(value = "AdminReservationTimeController")
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createReservationTime(
            @Valid @RequestBody ReservationTimeCreationRequest request
    ) {
        long savedId = reservationTimeService.saveReservationTime(request);
        ReservationTime savedTime = reservationTimeService.getReservationTimeById(savedId);
        return ResponseEntity
                .created(URI.create("/times/" + savedId))
                .body(new ReservationTimeResponse(savedTime));
    }

    @DeleteMapping("/{reservationTimeId}")
    public ResponseEntity<Void> deleteReservationTime(
            @PathVariable("reservationTimeId") Long id
    ) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
