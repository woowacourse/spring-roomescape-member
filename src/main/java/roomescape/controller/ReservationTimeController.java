package roomescape.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.reservationtime.AvailableReservationTimesQuery;
import roomescape.controller.dto.reservationtime.AvailableReservationTimesResponse;
import roomescape.controller.dto.reservationtime.ReservationTimeRequest;
import roomescape.controller.dto.reservationtime.ReservationTimeResponse;
import roomescape.controller.dto.reservationtime.ReservationTimeResponses;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.reservationtime.AvailableReservationTimesResult;
import roomescape.service.dto.reservationtime.ReservationTimeResult;

@RestController
@RequestMapping("/times")
@RequiredArgsConstructor
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    @GetMapping
    public ResponseEntity<ReservationTimeResponses> getReservationTimes() {
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.getReservationTimes().stream()
                .map(ReservationTimeResponse::from)
                .toList();
        return ResponseEntity.ok(new ReservationTimeResponses(reservationTimes));
    }

    @GetMapping(params = {"themeId", "date"})
    public ResponseEntity<AvailableReservationTimesResponse> getAvailableReservationTimes(
            @RequestParam Long themeId,
            @RequestParam String date,
            @RequestParam(required = false) Boolean available
    ) {
        AvailableReservationTimesResult result = reservationTimeService.getAvailableReservationTimes(
                AvailableReservationTimesQuery.toQuery(themeId, date, available).toCondition());
        return ResponseEntity.ok(AvailableReservationTimesResponse.from(result));
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createReservationTime(
            @Valid @RequestBody ReservationTimeRequest request) {
        ReservationTimeResult reservationTimeResult = reservationTimeService.createReservationTime(request.toCommand());
        ReservationTimeResponse reservationTime = ReservationTimeResponse.from(reservationTimeResult);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
