package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.dto.TimeRequest;
import roomescape.dto.TimeResponse;
import roomescape.service.TimeService;

@Controller
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<TimeResponse>> readAll() {
        List<TimeResponse> reservationTimes = timeService.readAll();
        return ResponseEntity.ok().body(reservationTimes);
    }

    @GetMapping("/{themeId}")
    @ResponseBody
    public ResponseEntity<List<TimeResponse>> readAllByThemeIdAndDate(@PathVariable Long themeId,
                                                                      @RequestParam("date") String date) {
        List<TimeResponse> reservationTimes = timeService.readAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(reservationTimes);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<TimeResponse> register(@RequestBody TimeRequest timeRequest) {
        TimeResponse timeResponse = timeService.register(timeRequest);
        return ResponseEntity.created(URI.create("/times/" + timeResponse.id())).body(timeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeById(@PathVariable Long id) {
        timeService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
