package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.dto.request.ReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@RestController
@RequestMapping(value = "/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping()
    public List<ReservationResponse> findAll() {
        return reservationService.findAll();
    }

    @PostMapping()
    public Long create(@Valid @RequestBody ReservationCreateRequest reservationCreateRequest) {
        return reservationService.create(reservationCreateRequest);
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return reservationService.delete(id);
    }
}
