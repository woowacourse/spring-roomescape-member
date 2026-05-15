package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.controller.dto.ReservationUpdateRequest;
import roomescape.domain.Reservation;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponse> findAll(@RequestParam(required = false) String name) {
        List<Reservation> reservations = (name != null)
                ? reservationService.findByName(name)
                : reservationService.findAll();
        return reservations.stream()
                .map(ReservationResponse::toDto)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse create(@RequestBody ReservationCreateRequest dto) {
        Reservation reservation = reservationService.reserve(dto);
        return ReservationResponse.toDto(reservation);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponse update(@PathVariable Long id, @RequestBody ReservationUpdateRequest dto) {
        Reservation reservation = reservationService.update(id, dto);
        return ReservationResponse.toDto(reservation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reservationService.cancel(id);
    }
}
