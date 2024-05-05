package roomescape.controller;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.responseDto.AvailableTimeResponse;
import roomescape.requestDto.ReservationCreateRequest;
import roomescape.responseDto.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAllReservations() {
        List<ReservationResponse> reservationResponses = reservationService.findAll();

        return ResponseEntity.ok()
                .body(reservationResponses);
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableTimeResponse>> findAllAvailableTimes(
            @RequestParam LocalDate date, @RequestParam String themeId) {
        List<AvailableTimeResponse> availableTimeResponses = reservationService.findByDateAndThemeId(date,
                Long.valueOf(themeId));
        return ResponseEntity.ok()
                .body(availableTimeResponses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody ReservationCreateRequest reservationCreateRequest) {
        ReservationResponse reservationResponse = reservationService.create(reservationCreateRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent()
                .build();
    }

    @ExceptionHandler(value = DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException e) {
        return new ResponseEntity<>(e.getParsedString() + "은(는) 올바른 시간 형식이 아닙니다.", HttpStatus.BAD_REQUEST);
    }
}
