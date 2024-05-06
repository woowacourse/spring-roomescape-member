package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
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
import roomescape.controller.request.ReservationTimeWebRequest;
import roomescape.controller.response.ReservationTimeUserWebResponse;
import roomescape.controller.response.ReservationTimeWebResponse;
import roomescape.service.ReservationTimeService;
import roomescape.service.request.ReservationTimeAppRequest;
import roomescape.service.response.ReservationTimeAppResponse;
import roomescape.service.response.ReservationTimeAppResponseWithBookable;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeWebResponse> create(@Valid @RequestBody ReservationTimeWebRequest request) {
        ReservationTimeAppResponse appResponse = reservationTimeService.save(
            new ReservationTimeAppRequest(request.startAt()));
        Long id = appResponse.id();

        return ResponseEntity.created(URI.create("/times/" + id))
            .body(new ReservationTimeWebResponse(id, appResponse.startAt()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBy(@PathVariable Long id) {
        reservationTimeService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeWebResponse>> getReservationTimes() {
        List<ReservationTimeAppResponse> appResponses = reservationTimeService.findAll();

        List<ReservationTimeWebResponse> reservationTimeWebResponses = appResponses.stream()
            .map(appResponse -> new ReservationTimeWebResponse(appResponse.id(),
                appResponse.startAt()))
            .toList();

        return ResponseEntity.ok(reservationTimeWebResponses);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReservationTimeUserWebResponse>> getReservationTimesWithAvailability(
        @RequestParam LocalDate date, @RequestParam Long id) {

        List<ReservationTimeAppResponseWithBookable> appResponses = reservationTimeService
            .findAllWithBookAvailability(date, id);

        List<ReservationTimeUserWebResponse> webResponses = appResponses.stream()
            .map(response -> new ReservationTimeUserWebResponse(
                response.id(),
                response.startAt(),
                response.alreadyBooked()))
            .toList();

        return ResponseEntity.ok(webResponses);
    }
    
}
