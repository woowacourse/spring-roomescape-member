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
import roomescape.dto.AddReservationTimeDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponseDto>> allReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.allReservationTimes();
        List<ReservationTimeResponseDto> reservationTimeResponseDtos = reservationTimes.stream()
                .map((reservationTime) -> new ReservationTimeResponseDto(reservationTime.getId(),
                        reservationTime.getTime()))
                .toList();

        return ResponseEntity.ok(reservationTimeResponseDtos);
    }

    @PostMapping
    public ResponseEntity<Void> addReservationTime(@RequestBody @Valid AddReservationTimeDto newReservationTimeDto) {
        reservationTimeService.addReservationTime(newReservationTimeDto);
        return ResponseEntity.created(URI.create("/times/")).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservations(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }
}
