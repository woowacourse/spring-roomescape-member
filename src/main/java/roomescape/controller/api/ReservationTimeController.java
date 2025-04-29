package roomescape.controller.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.request.CreateReservationTimeRequest;
import roomescape.controller.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.ReservationTimeCreation;

@RequestMapping("/times")
@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ReservationTimeResponse addReservationTime(
            @RequestBody CreateReservationTimeRequest request) {
        ReservationTimeCreation creation = ReservationTimeCreation.from(request);
        return reservationTimeService.addReservationTime(creation);
    }

    @GetMapping
    public List<ReservationTimeResponse> findAll() {
        return reservationTimeService.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable long id) {
        boolean deleted = reservationTimeService.deleteById(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
