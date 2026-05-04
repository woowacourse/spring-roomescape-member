package roomescape.reservation.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.controller.dto.ReservationResponseDto;
import roomescape.reservation.controller.dto.ReservationSaveRequestDto;
import roomescape.reservation.service.ReservationService;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponseDto> getReservations() {
        return reservationService.getReservations().stream()
                .map(ReservationResponseDto::from)
                .collect(Collectors.toList());
    }

    @PostMapping("/reservations")
    public ReservationResponseDto saveReservation(@RequestBody ReservationSaveRequestDto reservationRequest) {
        return ReservationResponseDto.from(reservationService.save(reservationRequest.toServiceDto()));
    }

    @DeleteMapping("/reservations/{id}")
    public void deleteReservation(@PathVariable long id) {
        reservationService.deleteById(id);
    }
}