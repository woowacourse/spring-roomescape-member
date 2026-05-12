package roomescape.domain.reservation.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservation.request.ReservationTimeCreateRequest;
import roomescape.domain.reservation.response.ReservationTimeResponse;
import roomescape.domain.reservation.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @Autowired
    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }


    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findAll() {
        List<ReservationTimeResponse> response = reservationTimeService.findAllReservationTimes();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> save(@RequestBody ReservationTimeCreateRequest request) {
        ReservationTimeResponse response = reservationTimeService.saveReservationTime(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.LOCATION, "/times/" + response.id())
                .body(response);
    }

    @DeleteMapping("/{reservationTimeId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long reservationTimeId) {
        reservationTimeService.deleteReservationTimeBy(reservationTimeId);
        return ResponseEntity.ok().build();
    }
}
