package roomescape.reservationtime.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.payload.ReservationTimeRequest;
import roomescape.reservationtime.payload.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationTimeResponse postTimes(@Valid @RequestBody ReservationTimeRequest request) {
        ReservationTime reservationTime = reservationTimeService.save(request);
        return ReservationTimeResponse.from(reservationTime);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationTimeResponse> getAvailableReservationTimes(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long themeId) {
        if (date == null && themeId == null) {
            return reservationTimeService.findAll()
                    .stream()
                    .map(ReservationTimeResponse::from)
                    .toList();
        }
        return reservationTimeService.findAvailableReservationTimes(date, themeId)
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTimes(@PathVariable Long id) {
        reservationTimeService.deleteById(id);
    }

}
