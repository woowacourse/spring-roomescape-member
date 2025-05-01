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
import roomescape.dto.BookedReservationTimeResponseDto;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.service.ReservationService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponseDto> readAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/reservations/dates/{date}/themes/{themeId}/times")
    public List<BookedReservationTimeResponseDto> readBookedReservationTimes(
        @PathVariable String date, @PathVariable Long themeId) {
        return reservationService.getAllBookedReservationTimes(date, themeId);
    }

    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDto createReservation(
        @RequestBody ReservationRequestDto reservationRequestDto) {
        return reservationService.saveReservation(reservationRequestDto);
    }

    @DeleteMapping("/reservations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable(name = "id") Long id) {
        reservationService.deleteReservation(id);
    }
}
