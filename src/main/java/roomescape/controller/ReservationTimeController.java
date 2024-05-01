package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.app.ReservationTimeAppRequest;
import roomescape.dto.web.ReservationTimeWebRequest;
import roomescape.dto.web.ReservationTimeWebResponse;
import roomescape.exception.reservationtime.DuplicatedReservationTimeException;
import roomescape.exception.reservationtime.ReservationExistsException;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @Autowired
    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeWebResponse> create(@RequestBody ReservationTimeWebRequest request) {
        ReservationTime newReservationTime = reservationTimeService.save(
            new ReservationTimeAppRequest(request.startAt()));
        Long id = newReservationTime.getId();

        return ResponseEntity.created(URI.create("/times/" + id))
            .body(new ReservationTimeWebResponse(
                id,
                newReservationTime.getStartAt()
            ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBy(@PathVariable Long id) {
        reservationTimeService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeWebResponse>> getReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.findAll();
        List<ReservationTimeWebResponse> reservationTimeWebResponses = reservationTimes.stream()
            .map(reservationTime -> new ReservationTimeWebResponse(reservationTime.getId(),
                reservationTime.getStartAt()))
            .toList();

        return ResponseEntity.ok(reservationTimeWebResponses);
    }

    @ExceptionHandler(ReservationExistsException.class)
    public ResponseEntity<String> handleReservationExistsException(ReservationExistsException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedReservationTimeException.class)
    public ResponseEntity<String> handleDuplicatedReservationTimeException(DuplicatedReservationTimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
