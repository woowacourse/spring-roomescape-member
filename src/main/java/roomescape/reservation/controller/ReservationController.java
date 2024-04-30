package roomescape.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservation.dto.ReservationResponseDto;
import roomescape.response.ResponseCode;
import roomescape.reservation.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponseDto> findAll() {
        return reservationService.findAll();
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> save(@RequestBody final ReservationRequestDto request) {
        ReservationResponseDto responseDto = reservationService.save(request);
        return ResponseEntity.created(URI.create("/reservations/" + responseDto.id())).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        final ResponseCode deletedStatus = reservationService.deleteById(id);
        return ResponseEntity.status(deletedStatus.getHttpStatus()).build();
    }
}
