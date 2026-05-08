package roomescape.domain.reservationtime;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.admin.AdminRequestValidator;
import roomescape.domain.reservationtime.dto.CreateTimeRequest;
import roomescape.domain.reservationtime.dto.CreateTimeResponse;
import roomescape.domain.reservationtime.dto.ReservationTimeAvailabilityResponse;
import roomescape.domain.reservationtime.dto.ReservationTimeResponse;

@RestController
@RequiredArgsConstructor
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;
    private final AdminRequestValidator validator;

    @GetMapping("/admin/times")
    public ResponseEntity<List<ReservationTimeResponse>> getAllReservationTime(HttpServletRequest request) {
        if (validator.isUnauthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<ReservationTimeResponse> response = reservationTimeService.getAllReservationTime();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/times")
    public ResponseEntity<CreateTimeResponse> createReservationTime(
        HttpServletRequest httpServletRequest,
        @RequestBody CreateTimeRequest createTimeRequest
    ) {
        if (validator.isUnauthorized(httpServletRequest)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        createTimeRequest.validate();
        CreateTimeResponse response = reservationTimeService.createReservationTime(createTimeRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/times/{id}")
    public ResponseEntity<Void> deleteReservationTime(HttpServletRequest request, @PathVariable Long id) {
        if (validator.isUnauthorized(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/times")
    public ResponseEntity<List<ReservationTimeAvailabilityResponse>> getReservationTimeAvailability(
        @RequestParam Long themeId,
        @RequestParam Long dateId
    ) {
        List<ReservationTimeAvailabilityResponse> response = reservationTimeService
            .getReservationTimeAvailability(themeId, dateId);
        return ResponseEntity.ok(response);
    }
}
