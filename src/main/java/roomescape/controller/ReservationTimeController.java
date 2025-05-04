package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeCreateRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.search.ReservationTimeResponseWithBookedStatus;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    @Autowired
    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }
    // todo: ReservationTime Response 처리
    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(@RequestBody ReservationTimeCreateRequest reservationTimeCreateRequest) {
        ReservationTimeResponse createdReservationTime = reservationTimeService.createReservationTime(reservationTimeCreateRequest);
        URI location = URI.create("/times/" + createdReservationTime.id());
        return ResponseEntity.created(location).body(createdReservationTime);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> read() {
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.findAll();
        return ResponseEntity.ok(reservationTimes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationTimeService.deleteReservationTimeById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReservationTimeResponseWithBookedStatus>> read(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long themeId) {
        List<ReservationTimeResponseWithBookedStatus> response = reservationTimeService.findAvailableReservationTimesByDateAndThemeId(
                date, themeId);
        return ResponseEntity.ok(response);
    }
}
