package roomescape.controller.reservation;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.controller.reservation.dto.CreateReservationRequest;
import roomescape.controller.reservation.dto.ReservationResponse;
import roomescape.service.ReservationService;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(final ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> reserve(@RequestBody CreateReservationRequest request) {
        //TODO : 날짜 바인딩 예외 응답 처리하기
        var reservation = service.reserve(request.name(), request.date(), request.timeSlotId(), request.themeId());
        var response = ReservationResponse.from(reservation);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> allReservations() {
        var reservations = service.allReservations();
        var response = ReservationResponse.from(reservations);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean isRemoved = service.removeById(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
