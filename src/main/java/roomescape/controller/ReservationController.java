package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ReservationCreateRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations() {
        List<ReservationResponseDto> allReservations = reservationService.findAllReservationResponses();
        return ResponseEntity.ok(allReservations);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> postReservation(@RequestBody final ReservationCreateRequestDto requestDto) {
        ReservationResponseDto responseDto = reservationService.createReservation(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") final Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
    }
}
