package roomescape.controller;

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
import roomescape.dto.AddReservationTimeDto;
import roomescape.dto.ReservationResponseTimeDto;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseTimeDto>> allReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.allReservationTimes();
        List<ReservationResponseTimeDto> reservationResponseTimeDtos = reservationTimes.stream()
                .map((reservationTime) -> new ReservationResponseTimeDto(reservationTime.getId(),
                        reservationTime.getTime()))
                .toList();

        return ResponseEntity.ok(reservationResponseTimeDtos);
    }

    @PostMapping
    public ResponseEntity<Void> addReservationTime(@RequestBody AddReservationTimeDto newReservationTimeDto) {
        Long addedReservationTimeId = reservationTimeService.addReservationTime(newReservationTimeDto);
        return ResponseEntity.created(URI.create("/reservations/" + addedReservationTimeId)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservations(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
