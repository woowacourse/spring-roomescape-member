package roomescape.controller.api;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.CreateReservationRequest;
import roomescape.controller.dto.CreateReservationResponse;
import roomescape.controller.dto.CreateThemeResponse;
import roomescape.controller.dto.CreateTimeResponse;
import roomescape.controller.dto.ErrorMessageResponse;
import roomescape.domain.Reservation;
import roomescape.exception.DuplicatedReservationException;
import roomescape.exception.PastReservationException;
import roomescape.exception.ReservationTimeNotFoundException;
import roomescape.exception.ThemeNotFoundException;
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
        Reservation newReservation = reservationService.save(
            new SaveReservationDto(request.name(), request.date(), request.timeId(), request.themeId()));
        Long id = newReservation.getId();

        CreateReservationResponse response = new CreateReservationResponse(id, newReservation.getName(),
            newReservation.getDate(),
            CreateTimeResponse.from(newReservation),
            CreateThemeResponse.from(newReservation));

        return ResponseEntity.created(URI.create("/reservations/" + id)).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CreateReservationResponse>> findAll() {
        List<Reservation> reservations = reservationService.findAll();
        List<CreateReservationResponse> createReservationResponse = reservations.stream().
            map(reservation -> new CreateReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                CreateTimeResponse.from(reservation),
                CreateThemeResponse.from(reservation)
            )).toList();

        return ResponseEntity.ok(createReservationResponse);
    }

    @ExceptionHandler(ReservationTimeNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleReservationTimeNotFoundException(
        ReservationTimeNotFoundException e) {
        ErrorMessageResponse response = new ErrorMessageResponse(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ThemeNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleThemeNotFoundException(ThemeNotFoundException e) {
        ErrorMessageResponse response = new ErrorMessageResponse(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(PastReservationException.class)
    public ResponseEntity<ErrorMessageResponse> handlePastReservationException(PastReservationException e) {
        ErrorMessageResponse response = new ErrorMessageResponse(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DuplicatedReservationException.class)
    public ResponseEntity<ErrorMessageResponse> handleDuplicatedReservationException(DuplicatedReservationException e) {
        ErrorMessageResponse response = new ErrorMessageResponse(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
