package roomescape.domain.reservation.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservation.dto.request.ReservationCreateRequestDto;
import roomescape.domain.reservation.dto.response.ReservationCreateResponseDto;
import roomescape.domain.reservation.dto.response.ReservationResponseDto;
import roomescape.domain.reservation.service.ReservationService;

@RestController
@RequestMapping("/api/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping()
    public ResponseEntity<List<ReservationResponseDto>> getReservations() {
        return ResponseEntity.status(HttpStatus.OK)
            .body(reservationService.getReservations());
    }

    @PostMapping()
    public ResponseEntity<ReservationCreateResponseDto> saveReservation(
        @RequestBody ReservationCreateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(reservationService.saveReservation(requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservationById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

}
