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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.request.ReservationSaveDto;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    @Operation(summary = "Create a reservation", description = "예약을 생성하는 api")
    public ResponseEntity<ReservationResponse> create(
            @Valid @RequestBody ReservationSaveDto dto
    ) {
        ReservationResponse responseData = ReservationResponse.from(
                reservationService.create(dto.name(), dto.date(), dto.timeId(), dto.themeId()));
        return ResponseEntity.status(CREATED).body(responseData);
    }

    @GetMapping("/reservations")
    @Operation(summary = "Read reservations by name", description = "예약자 이름으로 예약 목록을 조회하는 api")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(@RequestParam String name) {
        List<ReservationResponse> responseData = reservationService.readAllByName(name).stream()
                .map(ReservationResponse::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/reservations/{id}")
    @Operation(summary = "Cancel a reservation", description = "예약을 취소하는 api")
    public ResponseEntity<ReservationResponse> cancel(@PathVariable Long id) {
        ReservationResponse responseData = ReservationResponse.from(reservationService.cancel(id));
        return ResponseEntity.ok(responseData);
    }
}
