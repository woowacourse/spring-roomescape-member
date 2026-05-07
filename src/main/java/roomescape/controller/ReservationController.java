package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.service.ReservationService;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse create(@RequestBody ReservationCreateRequest request) {
        Reservation reservation = reservationService.reserve(request);

        return ReservationResponse.toDto(reservation);
    }

    @GetMapping("/reservations")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationService.findAll();

        return reservations.stream()
                .map(ReservationResponse::toDto)
                .toList();
    }

    @DeleteMapping("/admin/reservations/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        reservationService.cancel(id);
    }
}
