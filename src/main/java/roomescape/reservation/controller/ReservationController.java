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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.request.ReservationSaveDto;
import roomescape.reservation.dto.response.ReservationDetailDto;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @Operation(summary = "Create a reservation", description = "예약을 생성하는 api")
    public ResponseEntity<ReservationDetailDto> createReservation(
            @Valid @RequestBody ReservationSaveDto dto
    ) {
        ReservationDetailDto responseData = ReservationDetailDto.from(
                reservationService.create(dto.name(), dto.date(), dto.timeId(), dto.themeId()));
        return ResponseEntity.status(CREATED).body(responseData);
    }

    @GetMapping
    @Operation(summary = "Read reservations by name", description = "예약자 이름으로 예약 목록을 조회하는 api")
    public ResponseEntity<List<ReservationDetailDto>> getMyReservations(@RequestParam String name) {
        List<ReservationDetailDto> responseData = reservationService.findAllByName(name).stream()
                .map(ReservationDetailDto::from)
                .toList();
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Cancel a reservation", description = "예약을 취소하는 api")
    public ResponseEntity<ReservationDetailDto> cancelReservation(@PathVariable Long id) {
        ReservationDetailDto responseData = ReservationDetailDto.from(reservationService.cancel(id));
        return ResponseEntity.ok(responseData);
    }
}
