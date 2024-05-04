package roomescape.reservation.controller;

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

import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservation.dto.ReservationResponseDto;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> findAll() {
        List<ReservationResponseDto> responseDtoList = reservationService.findAll().stream()
                                                                         .map(ReservationResponseDto::new)
                                                                         .toList();
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> save(@RequestBody final ReservationRequestDto request) {
        ReservationResponseDto responseDto = new ReservationResponseDto(reservationService.save(request));
        return ResponseEntity.created(URI.create("/reservations/" + responseDto.id())).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        reservationService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
