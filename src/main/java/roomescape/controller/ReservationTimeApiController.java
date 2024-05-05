package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.AvailabilityOfTimeRequestDto;
import roomescape.service.dto.AvailabilityOfTimeResponseDto;
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

    @GetMapping("/availability")
    public List<AvailabilityOfTimeResponseDto> findReservationTimesAvailability(@RequestParam String date,
                                                                                @RequestParam Long themeId) {
        return reservationTimeService.findReservationTimesAvailability(new AvailabilityOfTimeRequestDto(date, themeId));
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponseDto> createReservationTime(
            @RequestBody ReservationTimeRequestDto requestDto) {
        ReservationTimeResponseDto reservationTime = reservationTimeService.createReservationTime(requestDto);
        return ResponseEntity.created(URI.create("/times/" + reservationTime.getId())).body(reservationTime);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteReservationTime(@PathVariable long id) {
        reservationTimeService.deleteReservationTime(id);
    }
}
