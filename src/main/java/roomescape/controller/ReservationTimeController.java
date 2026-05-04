package roomescape.controller;

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
import roomescape.domain.ReservationTime;
import roomescape.dto.reservationTime.ReservationTimeRequesetDto;
import roomescape.dto.reservationTime.ReservationTimeResponseDto;
import roomescape.service.ReservationService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationService reservationService;

    public ReservationTimeController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationTimeResponseDto> getReservationTimes() {
        return reservationService.getReservationTimes().stream()
                .map(ReservationTimeResponseDto::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ReservationTimeResponseDto addReservationTime(@RequestBody ReservationTimeRequesetDto requestDto) {
        ReservationTime time = reservationService.addReservationTime(requestDto);
        return ReservationTimeResponseDto.from(time);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReservationTime(@PathVariable Long id) {
        reservationService.deleteReservationTime(id);
    }
}
