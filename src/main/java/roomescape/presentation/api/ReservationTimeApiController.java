package roomescape.presentation.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.AuthRequired;
import roomescape.auth.Role;
import roomescape.business.model.entity.ReservationTime;
import roomescape.business.model.vo.UserRole;
import roomescape.business.service.ReservationTimeService;
import roomescape.presentation.dto.request.ReservationTimeRequest;
import roomescape.presentation.dto.response.ReservationTimeResponse;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
public class ReservationTimeApiController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeApiController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping("/times")
    @AuthRequired
    @Role(UserRole.ADMIN)
    public ResponseEntity<ReservationTimeResponse> createReservationTime(@RequestBody @Valid ReservationTimeRequest request) {
        ReservationTime reservationTime = reservationTimeService.addAndGet(request.startAtToLocalTime());
        ReservationTimeResponse response = ReservationTimeResponse.from(reservationTime);
        return ResponseEntity.created(URI.create("/times")).body(response);
    }

    @GetMapping("/times")
    @AuthRequired
    public ResponseEntity<List<ReservationTimeResponse>> getAllReservationTime() {
        List<ReservationTime> reservationTimes = reservationTimeService.getAll();
        List<ReservationTimeResponse> responses = ReservationTimeResponse.from(reservationTimes);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/times/possible")
    @AuthRequired
    public ResponseEntity<List<ReservationTimeResponse>> getAvailableReservationTimes(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") long themeId
    ) {
        List<ReservationTime> reservationTimes = reservationTimeService.getAvailableReservationTimesByDateAndThemeId(date, themeId);
        List<ReservationTimeResponse> responses = ReservationTimeResponse.from(reservationTimes);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/times/{id}")
    @AuthRequired
    @Role(UserRole.ADMIN)
    public ResponseEntity<Void> deleteReservationTime(@PathVariable long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
