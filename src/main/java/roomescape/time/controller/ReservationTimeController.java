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

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> saveTime(@RequestBody ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeService.saveTime(reservationTimeRequest.toReservationTime());

        ReservationTimeResponse reservationTimeResponse = changeToReservationTimeResponse(reservationTime);
        String url = "/times/" + reservationTimeResponse.id();

        return ResponseEntity.created(URI.create(url)).body(reservationTimeResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findTimeList() {
        List<ReservationTime> reservationTimes = reservationTimeService.findTimeList();

        List<ReservationTimeResponse> reservationTimeResponses = changeToReservationTimeResponses(reservationTimes);

        return ResponseEntity.ok(reservationTimeResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeById(@PathVariable("id") long id) {
        reservationTimeService.deleteTimeById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<ReservationTimeStatusResponse>> findTimeByDateAndThemeId(@RequestParam("date") String date, @RequestParam("themeId") long themeId) {
        List<ReservationTimeStatus> reservationTimeStatuses = reservationTimeService.findTimeListByDateAndThemeId(date, themeId);

        List<ReservationTimeStatusResponse> reservationTimeStatusResponses = changeToReservationTimeStatusResponses(reservationTimeStatuses);

        return ResponseEntity.ok(reservationTimeStatusResponses);
    }

    private ReservationTimeResponse changeToReservationTimeResponse(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime);
    }

    private List<ReservationTimeResponse> changeToReservationTimeResponses(List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(this::changeToReservationTimeResponse)
                .toList();
    }

    private ReservationTimeStatusResponse changeToReservationTimeStatusResponse(ReservationTimeStatus reservationTimeStatus) {
        return new ReservationTimeStatusResponse(reservationTimeStatus);
    }

    private List<ReservationTimeStatusResponse> changeToReservationTimeStatusResponses(List<ReservationTimeStatus> reservationTimeStatuses) {
        return reservationTimeStatuses.stream()
                .map(this::changeToReservationTimeStatusResponse)
                .toList();
    }
}
