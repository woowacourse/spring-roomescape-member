package roomescape.controller.user;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.TimeResponse;
import roomescape.service.TimeService;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping(params = {"themeId", "date"})
    public ResponseEntity<List<TimeResponse>> readAllByThemeIdAndDate(@RequestParam Long themeId,
                                                                      @RequestParam String date) {
        List<TimeResponse> reservationTimes = timeService.readAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(reservationTimes);
    }

}
