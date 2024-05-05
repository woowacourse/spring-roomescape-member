package roomescape.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.time.TimeRequest;
import roomescape.dto.time.TimeResponse;
import roomescape.dto.time.TimesResponse;
import roomescape.global.dto.response.ApiResponse;
import roomescape.service.TimeService;

@RestController
public class TimeController {

    private final TimeService timeService;

    public TimeController(final TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/times")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TimesResponse> getAllTimes() {

        return ApiResponse.success(timeService.findAllTimes());
    }

    @PostMapping("/times")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TimeResponse> saveTime(
            @RequestBody final TimeRequest timeRequest,
            HttpServletResponse response
    ) {
        TimeResponse timeResponse = timeService.addTime(timeRequest);
        response.setHeader("Location", "/times/" + timeResponse.id());

        return ApiResponse.success(timeResponse);
    }

    @DeleteMapping("/times/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> removeTime(@PathVariable final Long id) {
        timeService.removeTimeById(id);

        return ApiResponse.success();
    }
}
