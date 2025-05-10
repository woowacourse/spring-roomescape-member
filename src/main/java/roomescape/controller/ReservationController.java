package roomescape.controller;

import jakarta.validation.Valid;
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
import roomescape.domain.LoginMember;
import roomescape.dto.AvailableReservationTimeRequestDto;
import roomescape.dto.AvailableReservationTimeResponseDto;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.service.ReservationService;

@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponseDto> readAllReservations() {
        return reservationService.getAllReservations();
    }

    @PostMapping("/available-times")
    public List<AvailableReservationTimeResponseDto> readAvailableReservationTimes(
            @RequestBody AvailableReservationTimeRequestDto request) {
        return reservationService.getAvailableReservationTimes(request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDto createReservation(
            @RequestBody @Valid ReservationRequestDto request, LoginMember member) {
        return reservationService.saveReservation(request, member);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable(name = "id") Long id) {
        reservationService.deleteReservation(id);
    }
}
