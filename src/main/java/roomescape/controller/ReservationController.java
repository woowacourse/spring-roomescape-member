package roomescape.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Reservation;
import roomescape.domain.vo.MemberName;
import roomescape.dto.ResourceIdResponseDto;
import roomescape.dto.reservation.ReservationRequestDto;
import roomescape.dto.reservation.ReservationResponseDto;
import roomescape.dto.reservation.ReservationsResponseDto;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ReservationsResponseDto getReservations(
            @RequestParam(value = "name", required = false) String name
    ) {
        if (name != null) {
            return new ReservationsResponseDto(reservationService.findReservationsByName(new MemberName(name)).stream()
                    .map(ReservationResponseDto::from)
                    .toList());
        }

        return new ReservationsResponseDto(reservationService.getReservations().stream()
                .map(ReservationResponseDto::from)
                .toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceIdResponseDto addReservation(
            @Valid @RequestBody ReservationRequestDto requestDto
    ) {
        Reservation reservation = reservationService.addReservation(requestDto);
        return new ResourceIdResponseDto(reservation.getId());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(
            @PathVariable Long id
    ) {
        reservationService.deleteReservation(id);
    }
}
