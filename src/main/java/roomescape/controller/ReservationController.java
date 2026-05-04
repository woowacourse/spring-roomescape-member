package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.reservation.ReservationRequestDto;
import roomescape.dto.reservation.ReservationResponseDto;
import roomescape.dto.reservationTime.ReservationTimeRequesetDto;
import roomescape.dto.reservationTime.ReservationTimeResponseDto;
import roomescape.service.ReservationService;

import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponseDto> getReservations() {
        return reservationService.getReservations();
    }

    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponseDto addReservation(@RequestBody ReservationRequestDto requestDto) {
        return reservationService.addReservation(requestDto);
    }

    @DeleteMapping("/reservations/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteReservation(id);
    }

    @GetMapping("/times")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationTimeResponseDto> getReservationTimes() {
        return reservationService.getReservationTimes();
    }

    @PostMapping("/times")
    @ResponseStatus(HttpStatus.OK)
    public ReservationTimeResponseDto addReservationTime(@RequestBody ReservationTimeRequesetDto requestDto) {
        return reservationService.addReservationTime(requestDto);
    }

    @DeleteMapping("/times/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReservationTime(@PathVariable("id") Long id) {
        reservationService.deleteReservationTime(id);
    }
}
