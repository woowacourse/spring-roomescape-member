package roomescape.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservation.dto.ReservationResponseDto;
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
    public ResponseEntity<List<ReservationResponseDto>> findAll() {
        final List<Reservation> reservations = reservationService.readAll();

        final List<ReservationResponseDto> reservationResponseDtos = changeToReservationResponseDtos(reservations);

        return ResponseEntity.ok(reservationResponseDtos);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> save(@RequestBody final ReservationRequestDto reservationRequestDto, Member member) {
        final Reservation reservation = reservationService.create(reservationRequestDto.toReservation(member));

        final ReservationResponseDto reservationResponseDto = changeToReservationResponseDto(reservation);
        final String url = "/reservations/" + reservationResponseDto.id();

        return ResponseEntity.created(URI.create(url)).body(reservationResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final long id) {
        reservationService.delete(id);

        return ResponseEntity.noContent().build();
    }

    private ReservationResponseDto changeToReservationResponseDto(final Reservation reservation) {
        return new ReservationResponseDto(reservation);
    }

    private List<ReservationResponseDto> changeToReservationResponseDtos(final List<Reservation> reservations) {
        return reservations.stream()
                .map(this::changeToReservationResponseDto)
                .toList();
    }
}
