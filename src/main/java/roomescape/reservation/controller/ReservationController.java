package roomescape.reservation.controller;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.controller.dto.ReservationResponseDto;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.controller.dto.ReservationSaveRequestDto;
import roomescape.reservation.controller.dto.ReservationUpdateRequestDto;
import roomescape.reservation.service.ReservationService;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponseDto>> getAll(
            @RequestParam(required = false) String name) {
        List<Reservation> reservations;
        if (name != null) {
            reservations = reservationService.findByName(name);
        } else {
            reservations = reservationService.getAll();
        }
        List<ReservationResponseDto> body = reservations.stream()
                .map(ReservationResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponseDto> create(
            @Valid @RequestBody ReservationSaveRequestDto reservationRequest) {
        ReservationResponseDto body = ReservationResponseDto.from(
                reservationService.create(reservationRequest.toServiceDto()));
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ReservationUpdateRequestDto request) {
        ReservationResponseDto body = ReservationResponseDto.from(
                reservationService.update(request.toServiceDto(id)));
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> cancel(
            @PathVariable Long id,
            @RequestParam(required = false) String name) {
        if (name == null) {
            reservationService.cancel(id);
        } else {
            reservationService.cancel(id, name);
        }
        return ResponseEntity.noContent().build();
    }
}
