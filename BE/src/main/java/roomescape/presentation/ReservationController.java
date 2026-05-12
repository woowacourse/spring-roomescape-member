package roomescape.presentation;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.application.ReservationService;
import roomescape.entity.Reservation;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.customException.InvalidRequestException;
import roomescape.presentation.dto.AvailableReservationResponse;
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationResponse;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody ReservationRequest request
    ) {
        Reservation created = reservationService.save(
                request.name(),
                request.date(),
                request.timeId(),
                request.themeId()
        );

        ReservationResponse response = ReservationResponse.from(created);
        return ResponseEntity.created(parseCreatedResourceURI(response))
                .body(response);
    }

    private URI parseCreatedResourceURI(ReservationResponse response) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readAllReservations() {
        List<Reservation> reservations = reservationService.findAll();
        List<ReservationResponse> response = convertToReservationResponse(reservations);
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = {"date", "themeId"})
    public ResponseEntity<List<AvailableReservationResponse>> readReservationsByDateAndTheme(
            @RequestParam LocalDate date, @RequestParam Long themeId) {
        List<Reservation> reservations = reservationService.findAllByDateAndThemeId(date, themeId);
        List<AvailableReservationResponse> response = convertToAvailableReservationResponse(reservations);
        return ResponseEntity.ok(response);
    }

    private List<ReservationResponse> convertToReservationResponse(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private List<AvailableReservationResponse> convertToAvailableReservationResponse(List<Reservation> reservations) {
        return reservations.stream()
                .map(AvailableReservationResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        if (id == null) {
            throw new InvalidRequestException(ErrorCode.RESERVATION_ID_NULL);
        }
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
