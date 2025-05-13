package roomescape.presentation.member.api;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.member.ReservationService;
import roomescape.config.CurrentLoginMember;
import roomescape.config.LoginMember;
import roomescape.presentation.member.dto.ReservationRequestDto;
import roomescape.presentation.member.dto.ReservationResponseDto;

@RestController
@RequestMapping("/reservations")
public final class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getReservations() {
        List<ReservationResponseDto> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponseDto> getReservation(@PathVariable("reservationId") Long id) {
        ReservationResponseDto reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> reserve(@Valid @RequestBody ReservationRequestDto reservationDto,
                                                          @CurrentLoginMember LoginMember loginMember) {
        ReservationResponseDto reservation = reservationService.createReservation(reservationDto, loginMember);
        String location = "/reservations/" + reservation.id();
        return ResponseEntity.created(URI.create(location)).body(reservation);
    }
}
