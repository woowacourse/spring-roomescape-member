package roomescape.domain.reservation.controller;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.URLDecoder;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservation.dto.request.ReservationCreateRequestDto;
import roomescape.domain.reservation.dto.response.ReservationCreateResponseDto;
import roomescape.domain.reservation.dto.response.ReservationResponseDto;
import roomescape.domain.reservation.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getReservations(
        @RequestHeader("Reservation-Name") String name) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(reservationService.getReservationsByName(URLDecoder.decode(name, UTF_8)));
    }

    @PostMapping()
    public ResponseEntity<ReservationCreateResponseDto> saveReservation(
        @RequestBody ReservationCreateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(reservationService.saveReservation(requestDto));
    }
}
