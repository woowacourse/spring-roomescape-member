package roomescape.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.ReservationTimeRequestDto;
import roomescape.service.dto.ReservationTimeResponseDto;

@RestController
@RequestMapping("/times")
public class ReservationTimeApiController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeApiController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public List<ReservationTimeResponseDto> findReservationTimes() {
        return reservationTimeService.findAllReservationTimes();
    }

    @PostMapping
    public ReservationTimeResponseDto createReservationTime(@RequestBody ReservationTimeRequestDto requestDto) {
        return reservationTimeService.createReservationTime(requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteReservationTime(@PathVariable long id) {
        reservationTimeService.deleteReservationTime(id);
    }
}
