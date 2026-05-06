package roomescape.time.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.service.ThemeService;
import roomescape.time.controller.dto.ReservationTimeRequest;
import roomescape.time.controller.dto.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeService;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService, ThemeService themeService) {
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
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
        List<ReservationTimeResponse> responses = themeService.findAvailableTimes(themeId, date).availableTimeQueryResults()
                .stream()
                .map(ReservationTimeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/admin/times")
    public ResponseEntity<ReservationTimeResponse> create(@RequestBody ReservationTimeRequest requestDto) {
        ReservationTime reservationTime = reservationTimeService.save(requestDto.toCommand());
        ReservationTimeResponse response = ReservationTimeResponse.from(reservationTime);
        return ResponseEntity
                .created(URI.create("/times/" + reservationTime.getId()))
                .body(response);
    }

    @DeleteMapping("/admin/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
