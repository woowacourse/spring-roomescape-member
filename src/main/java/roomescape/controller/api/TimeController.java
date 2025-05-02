package roomescape.controller.api;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.TimeCreateRequest;
import roomescape.dto.TimeGetResponse;
import roomescape.exception.DuplicateTimeException;
import roomescape.service.TimeService;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;

    private TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TimeGetResponse createReservationTime(@RequestBody TimeCreateRequest timeCreateRequest) {
        return TimeGetResponse.from(timeService.createReservationTime(timeCreateRequest));
    }

    @GetMapping
    public List<TimeGetResponse> readAllReservationTimes() {
        return timeService.findAllReservationTimes().stream()
            .map(TimeGetResponse::from)
            .toList();
    }

    @GetMapping("/{date}/{themeId}")
    public List<TimeGetResponse> readReservationTimesByDateAndThemeIdWithIsBooked(@PathVariable("date") LocalDate date, @PathVariable("themeId") Long themeId) {
        return timeService.findReservationTimeByDateAndThemeIdWithIsBooked(date, themeId).stream()
            .map(TimeGetResponse::from)
            .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservationTimeById(@PathVariable("id") Long id) {
        timeService.deleteReservationTimeById(id);
    }

    @ExceptionHandler(value = DuplicateTimeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateException(DuplicateTimeException exception) {
        return exception.getMessage();
    }
}
