package roomescape.time.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.time.controller.dto.ReservationTimeRequest;
import roomescape.time.controller.dto.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeResponse>> readAll() {
        List<ReservationTimeResponse> responses = reservationTimeService.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping(value = "/times", params = "available=true")
    public ResponseEntity<List<ReservationTimeResponse>> readAvailable(
            @RequestParam("themeId") Long themeId,
            @RequestParam("date") LocalDate date
    ) {
        List<ReservationTimeResponse> responses = reservationTimeService.findAvailableTimes(themeId, date)
                .stream()
                .map(ReservationTimeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/admin/times")
    public ResponseEntity<ReservationTimeResponse> create(@RequestBody ReservationTimeRequest requestDto) {
        ReservationTime reservationTime = reservationTimeService.save(requestDto);
        ReservationTimeResponse response = ReservationTimeResponse.from(reservationTime);
        return ResponseEntity
                .created(URI.create("/times/" + response.id()))
                .body(response);
    }

    @DeleteMapping("/admin/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
