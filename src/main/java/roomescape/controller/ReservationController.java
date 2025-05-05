package roomescape.controller;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<ReservationResponse> readReservation() {
        return service.readReservation();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse postReservation(@RequestBody ReservationRequest request,
                                               HttpServletResponse response) {
        ReservationResponse reservationResponse = service.postReservation(request);
        response.setHeader("Location", "/reservations/" + reservationResponse.id());
        return reservationResponse;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable Long id) {
        service.deleteReservation(id);
    }
}
