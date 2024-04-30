package roomescape.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.AvailableTimeResponse;
import roomescape.service.AvailableTimeService;

@RestController
@RequestMapping("/availableTimes")
public class AvailableTimeController {

    private final AvailableTimeService availableTimeService;

    public AvailableTimeController(AvailableTimeService availableTimeService) {
        this.availableTimeService = availableTimeService;
    }

    @GetMapping()
    public List<AvailableTimeResponse> findByThemeAndDate(@RequestParam LocalDate date, @RequestParam long themeId) {
        return availableTimeService.findByThemeAndDate(date, themeId);
    }
}
