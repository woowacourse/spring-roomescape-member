package roomescape.domain.reservationtime.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.reservationtime.dto.request.ReservationTimeRequestDto;
import roomescape.domain.reservationtime.dto.response.BookedReservationTimeResponseDto;
import roomescape.domain.reservationtime.dto.response.ReservationTimeResponseDto;
import roomescape.domain.reservationtime.service.ReservationTimeService;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public List<ReservationTimeResponseDto> readReservationTimes() {
        return reservationTimeService.getAllReservationTimes();
    }

    @GetMapping("/themes/{themeId}/times")
    public List<BookedReservationTimeResponseDto> readBookedReservationTimes(
        @RequestParam String date, @PathVariable Long themeId) {
        return reservationTimeService.getTimesContainsReservationInfoBy(date, themeId);
    }

    @PostMapping("/times")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationTimeResponseDto saveReservationTime(
        @RequestBody ReservationTimeRequestDto reservationTimeRequestDto) {
        return reservationTimeService.saveReservationTime(reservationTimeRequestDto);
    }

    @DeleteMapping("/times/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservationTime(@PathVariable(name = "id") Long id) {
        reservationTimeService.deleteReservationTime(id);
    }
}
