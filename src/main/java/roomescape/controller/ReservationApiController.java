package roomescape.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationRequestDto;
import roomescape.service.dto.ReservationResponseDto;

@RestController
@RequestMapping("/reservations")
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponseDto> findReservations() {
        return reservationService.findAllReservations();
    }

    @PostMapping
    public ReservationResponseDto createReservation(@RequestBody ReservationRequestDto requestDto) {
        return reservationService.createReservation(requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable long id) {
        reservationService.deleteReservation(id);
    }
}
