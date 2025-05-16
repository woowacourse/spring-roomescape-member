package roomescape.reservationtime.controller;

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
import roomescape.reservationtime.controller.dto.AvailableReservationTimeResponse;
import roomescape.reservationtime.controller.dto.CreateReservationTimeRequest;
import roomescape.reservationtime.controller.dto.ReservationTimeResponse;
import roomescape.reservationtime.domain.AvailableReservationTime;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.reservationtime.service.dto.CreateReservationTimeServiceRequest;

@RequestMapping("/times")
@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationTimeResponse addReservationTime(
            @RequestBody CreateReservationTimeRequest request) {
        final CreateReservationTimeServiceRequest serviceRequest = request.toCreateReservationTimeServiceRequest();
        final ReservationTime savedReservationTime = reservationTimeService.addReservationTime(serviceRequest);
        return ReservationTimeResponse.from(savedReservationTime);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeService.findAllReservationTimes()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @GetMapping("/avaliable")
    @ResponseStatus(HttpStatus.OK)
    public List<AvailableReservationTimeResponse> findAvailableTime(@RequestParam(value = "date") LocalDate date,
                                                                    @RequestParam(value = "themeId") long themeId) {
        List<AvailableReservationTime> availableReservationTimes =
                reservationTimeService.findAllAvailableTime(date, themeId);

        return availableReservationTimes.stream()
                .map(AvailableReservationTimeResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservationTime(@PathVariable long id) {
        reservationTimeService.deleteById(id);
    }
}
