package roomescape.time.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.ReservationTimeRequest;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.dto.ReservationTimeStatus;
import roomescape.time.dto.ReservationTimeStatusResponse;
import roomescape.time.service.ReservationTimeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findAll() {
        final List<ReservationTime> reservationTimes = reservationTimeService.readAll();

        final List<ReservationTimeResponse> reservationTimeResponses = changeToReservationTimeResponses(reservationTimes);

        return ResponseEntity.ok(reservationTimeResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> save(@RequestBody final ReservationTimeRequest reservationTimeRequest) {
        final ReservationTime reservationTime = reservationTimeService.create(reservationTimeRequest.toReservationTime());

        final ReservationTimeResponse reservationTimeResponse = changeToReservationTimeResponse(reservationTime);
        final String url = "/times/" + reservationTimeResponse.id();

        return ResponseEntity.created(URI.create(url)).body(reservationTimeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        reservationTimeService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<ReservationTimeStatusResponse>> findAvailableTime(@RequestParam("date") final String date, @RequestParam("themeId") final long themeId) {
        final List<ReservationTimeStatus> reservationTimeStatuses = reservationTimeService.findAvailableTime(date, themeId);

        final List<ReservationTimeStatusResponse> reservationTimeStatusResponses = changeToReservationTimeStatusResponses(reservationTimeStatuses);

        return ResponseEntity.ok(reservationTimeStatusResponses);
    }

    private ReservationTimeResponse changeToReservationTimeResponse(final ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime);
    }

    private List<ReservationTimeResponse> changeToReservationTimeResponses(final List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(this::changeToReservationTimeResponse)
                .toList();
    }

    private ReservationTimeStatusResponse changeToReservationTimeStatusResponse(final ReservationTimeStatus reservationTimeStatus) {
        return new ReservationTimeStatusResponse(reservationTimeStatus);
    }

    private List<ReservationTimeStatusResponse> changeToReservationTimeStatusResponses(final List<ReservationTimeStatus> reservationTimeStatuses) {
        return reservationTimeStatuses.stream()
                .map(this::changeToReservationTimeStatusResponse)
                .toList();
    }
}
