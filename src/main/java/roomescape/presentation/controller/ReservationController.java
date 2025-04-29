package roomescape.presentation.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.presentation.dto.request.ReservationRequest;
import roomescape.presentation.dto.response.ReservationResponse;
import roomescape.application.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReservationResponse> getAllReservations() {
        return service.getAllReservations();
    }

    @PostMapping
    public ReservationResponse addReservation(@Valid @RequestBody ReservationRequest request) {
        return service.registerReservation(request);
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable("id") Long id) {
        service.deleteReservation(id);
    }
}
