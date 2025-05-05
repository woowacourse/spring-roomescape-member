package roomescape.presentation.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.TimeService;
import roomescape.application.dto.TimeDto;
import roomescape.domain.repository.dto.TimeDataWithBookingInfo;
import roomescape.presentation.dto.request.TimeRequest;
import roomescape.presentation.dto.response.TimeResponse;
import roomescape.presentation.dto.response.TimeResponseWithBookingInfo;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeService service;

    public TimeController(TimeService service) {
        this.service = service;
    }

    @GetMapping
    public List<TimeResponse> getAllTimes() {
        List<TimeDto> times = service.getAllTimes();
        return TimeResponse.from(times);
    }

    @PostMapping
    public ResponseEntity<TimeResponse> addTime(@Valid @RequestBody TimeRequest request) {
        TimeDto timeDto = service.registerNewTime(request);
        TimeResponse timeResponse = TimeResponse.from(timeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(timeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable(name = "id") Long id) {
        service.deleteTime(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/booking-status")
    public ResponseEntity<List<TimeResponseWithBookingInfo>> getTimesWithBookingInfo(
            @RequestParam LocalDate date, @RequestParam Long themeId) {
        List<TimeDataWithBookingInfo> timesWithBookingStatus = service.getTimesWithBookingInfo(date, themeId);
        List<TimeResponseWithBookingInfo> response = TimeResponseWithBookingInfo.from(timesWithBookingStatus);
        return ResponseEntity.ok().body(response);
    }
}
