package roomescape.controller.api;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exception.CannotCreatedException;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.NotCorrectDateTimeException;
import roomescape.exception.ThemeDoesNotExistException;
import roomescape.exception.TimeDoesNotExistException;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    private ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse createReservation(@RequestBody ReservationRequest request) {
        return ReservationResponse.from(reservationService.addReservationAfterNow(request));
    }

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse createAdminReservation(@RequestBody ReservationRequest request) {
        return ReservationResponse.from(reservationService.addReservation(request));
    }

    @GetMapping
    public List<ReservationResponse> readReservations() {
        return reservationService.findAllReservations().stream()
            .map(ReservationResponse::from)
            .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable Long id) {
        reservationService.removeReservation(id);
    }

    @ExceptionHandler(value = {TimeDoesNotExistException.class, ThemeDoesNotExistException.class,
        NotCorrectDateTimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleCreatedException(CannotCreatedException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(value = DuplicateReservationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateException(DuplicateReservationException ex) {
        return ex.getMessage();
    }
}
