package roomescape.reservation.controller;

import static org.springframework.http.HttpStatus.CREATED;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.request.ReservationSaveDto;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @Operation(summary = "Read all reservations", description = "예약 전체 목록을 조회하는 api")
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> responseData = reservationService.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @PostMapping
    @Operation(summary = "Create a reservation", description = "예약을 생성하는 api")
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationSaveDto dto) {
        ReservationResponse responseData = ReservationResponse.from(
                reservationService.create(dto.name(), dto.date(), dto.timeId(), dto.themeId()));
        return ResponseEntity.status(CREATED).body(responseData);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Cancel a reservation", description = "예약을 취소하는 api")
    public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable Long id) {
        ReservationResponse responseData = ReservationResponse.from(reservationService.cancel(id));
        return ResponseEntity.ok(responseData);
    }
}
