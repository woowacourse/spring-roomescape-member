package roomescape.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.controller.dto.request.ReservationCancelDto;
import roomescape.reservation.controller.dto.request.ReservationChangeScheduleDto;
import roomescape.reservation.controller.dto.request.ReservationSaveDto;
import roomescape.reservation.controller.dto.response.ReservationDetailDto;
import roomescape.reservation.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reservations")
    public ResponseEntity<ReservationDetailDto> create(@Validated @RequestBody ReservationSaveDto dto) {
        Reservation reservation = reservationService.reserve(dto.toCommand());
        ReservationDetailDto responseData = ReservationDetailDto.from(reservation);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/reservations/{name}")
    public ResponseEntity<List<ReservationDetailDto>> getMyReservations(@PathVariable String name) {
        List<ReservationDetailDto> responseData = reservationService.readAllByName(name).stream()
                .map(ReservationDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/reservations/{id}/cancel")
    public ResponseEntity<ReservationDetailDto> cancel(@PathVariable Long id, @Validated @RequestBody ReservationCancelDto dto) {
        Reservation reservation = reservationService.cancel(id, dto.name());
        ReservationDetailDto responseData = ReservationDetailDto.from(reservation);
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/reservations/{id}/schedule")
    public ResponseEntity<ReservationDetailDto> updateSchedule(
            @PathVariable Long id,
            @RequestParam String name,
            @Validated @RequestBody ReservationChangeScheduleDto dto
    ) {
        Reservation reservation = reservationService.changeSchedule(dto.toCommand(id, name));
        ReservationDetailDto responseData = ReservationDetailDto.from(reservation);
        return ResponseEntity.ok(responseData);
    }

}
