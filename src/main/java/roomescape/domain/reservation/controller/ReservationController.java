package roomescape.domain.reservation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservation.dto.request.ReservationCreateRequestDto;
import roomescape.domain.reservation.dto.response.ReservationByNameResponseDto;
import roomescape.domain.reservation.dto.response.ReservationCreateResponseDto;
import roomescape.domain.reservation.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
@Validated
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationByNameResponseDto>> getReservationsByName(
        @RequestParam
        @NotBlank(message = "예약자명은 필수입니다.")
        @Size(max = 20, message = "예약자명의 길이는 1이상 20이하 입니다.")
        String name
    ) {
        return ResponseEntity.ok(reservationService.getReservationsByName(name));
    }

    @PostMapping
    public ResponseEntity<ReservationCreateResponseDto> saveReservation(
        @Valid @RequestBody ReservationCreateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.saveReservation(requestDto));
    }
}
