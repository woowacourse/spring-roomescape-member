package roomescape.controller.api;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.CreateReservationRequest;
import roomescape.controller.dto.CreateReservationResponse;
import roomescape.controller.dto.FindReservationResponse;
import roomescape.domain.Reservation;
import roomescape.service.ReservationService;
import roomescape.service.dto.SaveReservationDto;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<CreateReservationResponse> save(@RequestBody CreateReservationRequest request) {
        Reservation newReservation = reservationService.save(new SaveReservationDto(request.name(),
            request.date(),
            request.timeId(),
            request.themeId())
        );

        Long id = newReservation.getId();

        CreateReservationResponse response = new CreateReservationResponse(
            id,
            newReservation.getName(),
            newReservation.getDate(),
            newReservation.getTime(),
            newReservation.getTheme()
        );

        return ResponseEntity.created(URI.create("/reservations/" + id)).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FindReservationResponse>> findAll() {
        List<Reservation> reservations = reservationService.findAll();
        List<FindReservationResponse> createReservationResponse = reservations.stream().
            map(reservation -> new FindReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
            )).toList();

        return ResponseEntity.ok(createReservationResponse);
    }
}
