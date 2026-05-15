package roomescape.controller.user;

import jakarta.validation.Valid;
<<<<<<< cycle2
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
=======
import org.springframework.http.ResponseEntity;
>>>>>>> bee9827
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
<<<<<<< cycle2
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
=======
>>>>>>> bee9827
import roomescape.domain.Reservation;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.dto.response.ReservationResponseDto;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> create(@Valid @RequestBody ReservationRequestDto reservationRequest) {
        Reservation reservation = reservationService.create(reservationRequest);
<<<<<<< cycle2
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(reservation.getId())
                .toUri();
        return ResponseEntity.created(uri)
                .body(ReservationResponseDto.from(reservation));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> findByName(@RequestParam String name) {
        List<ReservationResponseDto> responses = reservationService.findAllByName(name).stream()
                .map(ReservationResponseDto::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        reservationService.cancel(id);
        return ResponseEntity.noContent().build();
=======
        return ResponseEntity.ok(ReservationResponseDto.from(reservation));
>>>>>>> bee9827
    }
}
