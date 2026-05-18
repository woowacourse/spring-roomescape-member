package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.dto.ResourceIdResponseDto;
import roomescape.controller.dto.reservation.ReservationRequestDto;
import roomescape.controller.dto.reservation.ReservationResponseDto;
import roomescape.controller.dto.reservation.ReservationsResponseDto;
import roomescape.domain.Reservation;
import roomescape.domain.vo.MemberName;
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
            return new ReservationsResponseDto(reservationService.findReservationsByName(MemberName.from(name)).stream()
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
        Reservation reservation = reservationService.addReservation(requestDto.toCommand());
        return new ResourceIdResponseDto(reservation.getId());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "name", required = false) String name,
            @PathVariable Long id
    ) {
        if ("admin".equals(role)) {
            reservationService.deleteReservation(id);
            return;
        }

        reservationService.deleteReservation(id, MemberName.from(name));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateReservation(
            @RequestParam(value = "name") String name,
            @Valid @RequestBody ReservationRequestDto requestDto,
            @PathVariable Long id
    ) {
        reservationService.update(id, requestDto.toCommand(), MemberName.from(name));
    }
}
