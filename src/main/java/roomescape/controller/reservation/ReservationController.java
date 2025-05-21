package roomescape.controller.reservation;

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
import roomescape.domain.member.LoginMember;
import roomescape.dto.reservation.request.AvailableReservationTimeRequestDto;
import roomescape.dto.reservation.request.ReservationRequestDto;
import roomescape.dto.reservation.response.AvailableReservationTimeResponseDto;
import roomescape.dto.reservation.response.ReservationResponseDto;
import roomescape.service.reservation.ReservationService;

@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationResponseDto> readAllReservations() {
        return reservationService.getAllReservations();
    }

    @PostMapping("/available-times")
    public List<AvailableReservationTimeResponseDto> readAvailableReservationTimes(
            @RequestBody final AvailableReservationTimeRequestDto request) {
        return reservationService.getAvailableReservationTimes(request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDto createReservation(
            @RequestBody @Valid final ReservationRequestDto request, final LoginMember member) {
        return reservationService.saveReservation(request, member);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable(name = "id") final Long id) {
        reservationService.deleteReservation(id);
    }
}
