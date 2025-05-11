package roomescape.controller.api;

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
import roomescape.annotation.AdminOnly;
import roomescape.controller.dto.request.CreateReservationTimeRequest;
import roomescape.controller.dto.response.AvailableReservationTimeResponse;
import roomescape.controller.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;
import roomescape.service.dto.request.ReservationTimeCreation;
import roomescape.service.dto.response.ReservationTimeResult;

@RequestMapping("/times")
@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @AdminOnly
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationTimeResponse addReservationTime(@RequestBody @Valid CreateReservationTimeRequest request) {
        final ReservationTimeCreation creation = ReservationTimeCreation.from(request);
        ReservationTimeResult reservationTimeResult = reservationTimeService.addReservationTime(creation);
        return ReservationTimeResponse.from(reservationTimeResult);
    }

    @GetMapping
    public List<ReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeService.findAllReservationTimes()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @GetMapping("/available")
    public List<AvailableReservationTimeResponse> findAvailableTime(@RequestParam(value = "date") LocalDate date,
                                                                    @RequestParam(value = "themeId") long themeId) {
        return reservationTimeService.findAllAvailableTime(date, themeId)
                .stream()
                .map(AvailableReservationTimeResponse::from)
                .toList();
    }

    @AdminOnly
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservationTime(@PathVariable long id) {
        reservationTimeService.deleteById(id);
    }
}
