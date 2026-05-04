package roomescape.reservation.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.controller.dto.ReservationResponseDto;
import roomescape.reservation.controller.dto.ReservationSaveRequestDto;
import roomescape.reservation.service.ReservationService;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponseDto>> getAll() {
        List<ReservationResponseDto> body = reservationService.getAll().stream()
                .map(ReservationResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponseDto> create(
            @RequestBody ReservationSaveRequestDto reservationRequest) {
        ReservationResponseDto body = ReservationResponseDto.from(
                reservationService.create(reservationRequest.toServiceDto()));
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> cancel(@PathVariable long id) {
        reservationService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}