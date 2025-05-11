package roomescape.controller.reservationtime;

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
import roomescape.dto.reservationtime.request.ReservationTimeRequestDto;
import roomescape.dto.reservationtime.response.ReservationTimeResponseDto;
import roomescape.service.reservationtime.ReservationTimeService;

@RequestMapping("/times")
@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public List<ReservationTimeResponseDto> readReservationTimes() {
        return reservationTimeService.getAllReservationTimes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationTimeResponseDto saveReservationTime(@RequestBody @Valid ReservationTimeRequestDto request) {
        return reservationTimeService.saveReservationTime(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservationTime(@PathVariable(name = "id") Long id) {
        reservationTimeService.deleteReservationTime(id);
    }
}
