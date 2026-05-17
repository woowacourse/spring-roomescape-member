package roomescape.reservation.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.controller.dto.request.ReservationChangeScheduleDto;
import roomescape.reservation.controller.dto.response.ReservationDetailDto;
import roomescape.reservation.controller.dto.request.ReservationSaveDto;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ReservationAdminController {

    private final ReservationService reservationService;

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationDetailDto>> getReservations() {
        List<ReservationDetailDto> responseData = reservationService.readAll().stream()
                .map(ReservationDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationDetailDto> createReservation(@Valid @RequestBody ReservationSaveDto dto) {
        Reservation reservation = reservationService.reserve(dto.toCommand());
        ReservationDetailDto responseData = ReservationDetailDto.from(reservation);
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/reservations/{id}/cancel")
    public ResponseEntity<ReservationDetailDto> cancelReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.cancelByManager(id);
        ReservationDetailDto responseData = ReservationDetailDto.from(reservation);
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/reservations/{id}/schedule")
    public ResponseEntity<ReservationDetailDto> updateSchedule(
            @PathVariable Long id,
            @Validated @RequestBody ReservationChangeScheduleDto dto
    ) {
        Reservation reservation = reservationService.changeScheduleByManager(dto.toCommand(id));
        ReservationDetailDto responseData = ReservationDetailDto.from(reservation);
        return ResponseEntity.ok(responseData);
    }

}
