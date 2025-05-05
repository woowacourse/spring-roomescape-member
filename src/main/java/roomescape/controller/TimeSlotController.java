package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.controller.request.CreateTimeSlotRequest;
import roomescape.controller.response.TimeSlotResponse;
import roomescape.service.AvailableTimeSlotDto;
import roomescape.service.TimeSlotService;

@Controller
public class TimeSlotController {

    private final TimeSlotService service;

    public TimeSlotController(final TimeSlotService service) {
        this.service = service;
    }

    @PostMapping("/times")
    public ResponseEntity<TimeSlotResponse> register(@RequestBody @Valid CreateTimeSlotRequest request) {
        var timeSlot = service.register(request.startAt());
        var response = TimeSlotResponse.from(timeSlot);
        return ResponseEntity.created(URI.create("/times/" + timeSlot.id())).body(response);
    }

    @GetMapping("/times")
    public ResponseEntity<List<TimeSlotResponse>> getAllTimeSlots() {
        var timeSlots = service.findAllTimeSlots();
        var response = TimeSlotResponse.from(timeSlots);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/availableTimes", params = {"date", "themeId"})
    public ResponseEntity<List<AvailableTimeSlotDto>> getAvailableTimes(
        @RequestParam("date") LocalDate date,
        @RequestParam("themeId") Long themeId
    ) {
        var availableTimeSlots = service.findAvailableTimeSlots(date, themeId);
        return ResponseEntity.ok(availableTimeSlots);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean isRemoved = service.removeById(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
