package roomescape.reservation.presentation;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.exceptionHandler.dto.ExceptionResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> response = reservationService.getReservations();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody final ReservationRequest request) {
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.created(URI.create("admin/reservation")).body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservationById(@PathVariable("id") final Long id) {
        try {
            reservationService.deleteReservationById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ExceptionHandler(value = DateTimeParseException.class)
    public ResponseEntity<ExceptionResponse> noMatchDateType(final HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                400, "[ERROR] 요청 날짜 형식이 맞지 않습니다.", request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(exceptionResponse);
    }
}
