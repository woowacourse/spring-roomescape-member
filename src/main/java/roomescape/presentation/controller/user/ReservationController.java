package roomescape.presentation.controller.user;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.LoginInformation;
import roomescape.presentation.dto.request.ReservationRequestDto;
import roomescape.presentation.dto.response.ReservationResponseDto;
import roomescape.business.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public final class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> readReservations() {
        List<ReservationResponseDto> reservations = reservationService.readReservationAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponseDto> readReservation(@PathVariable("reservationId") Long id) {
        ReservationResponseDto reservation = reservationService.readReservationOne(id);
        return ResponseEntity.ok(reservation);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> create(LoginInformation login,
                                                         @Valid @RequestBody ReservationRequestDto reservationDto) {
        Long id = reservationService.createReservation(reservationDto, login.id());
        ReservationResponseDto reservation = reservationService.readReservationOne(id);
        String location = "/reservations/" + id;
        return ResponseEntity.created(URI.create(location)).body(reservation);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("reservationId") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
