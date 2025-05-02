package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.AddReservationTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> allReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.allReservationTimes();
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimes.stream()
                .map((reservationTime) -> new ReservationTimeResponse(reservationTime.getId(),
                        reservationTime.getTime()))
                .toList();

        return ResponseEntity.ok(reservationTimeResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> addReservationTime(
            @RequestBody @Valid AddReservationTimeRequest newReservationTimeDto) {
        ReservationTime addedReservationTime = reservationTimeService.addReservationTime(newReservationTimeDto);
        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(
                addedReservationTime.getId(), addedReservationTime.getTime());

        return ResponseEntity.created(URI.create("/times/")).body(reservationTimeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservations(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
