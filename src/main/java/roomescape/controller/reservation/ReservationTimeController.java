package roomescape.controller.reservation;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.security.Permission;
import roomescape.domain.member.Role;
import roomescape.domain.reservation.ReservationTime;
import roomescape.service.dto.reservation.ReservationTimeAvailabilityResponse;
import roomescape.service.dto.reservation.ReservationTimeResponse;
import roomescape.service.dto.reservation.ReservationTimeSaveRequest;
import roomescape.service.reservation.ReservationTimeService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/times")
@Permission(role = Role.ADMIN)
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.findReservationTimes().stream()
                .map(ReservationTimeResponse::of)
                .toList();
        ;
        return ResponseEntity.ok(reservationTimeResponses);
    }

    @GetMapping("/available")
    @Permission(role = Role.NONE)
    public ResponseEntity<List<ReservationTimeAvailabilityResponse>> getReservationTimeAvailability(@RequestParam LocalDate date,
                                                                                                    @RequestParam Long themeId) {
        List<ReservationTimeAvailabilityResponse> reservationTimeAvailabilities =
                reservationTimeService.findReservationTimeAvailability(date, themeId).stream()
                        .map(ReservationTimeAvailabilityResponse::of)
                        .toList();

        return ResponseEntity.ok(reservationTimeAvailabilities);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> addReservationTime(@RequestBody @Valid ReservationTimeSaveRequest request) {
        ReservationTime reservationTime = reservationTimeService.createReservationTime(request);
        return ResponseEntity.created(URI.create("/times/" + reservationTime.getId()))
                .body(ReservationTimeResponse.of(reservationTime));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
