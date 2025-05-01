package roomescape.controller.timeslot;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.service.dto.AvailableTimeSlotDto;
import roomescape.controller.timeslot.dto.CreateTimeSlotRequest;
import roomescape.controller.timeslot.dto.TimeSlotResponse;
import roomescape.service.TimeSlotService;

@Controller
public class TimeSlotController {

    private final TimeSlotService service;

    @Autowired
    public TimeSlotController(final TimeSlotService service) {
        this.service = service;
    }

    @PostMapping("/times")
    public ResponseEntity<TimeSlotResponse> add(@RequestBody CreateTimeSlotRequest request) {
        var timeSlot = service.add(request.startAt());
        var response = TimeSlotResponse.from(timeSlot);
        return ResponseEntity.created(URI.create("/times/" + timeSlot.id())).body(response);
    }

    @GetMapping("/times")
    public ResponseEntity<List<TimeSlotResponse>> allTimeSlots() {
        var timeSlots = service.allTimeSlots();
        var response = TimeSlotResponse.from(timeSlots);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean isRemoved = service.removeById(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/availableTimes", params = {"date", "themeId"})
    public ResponseEntity<List<AvailableTimeSlotDto>> availableTimes(
        @RequestParam("date") LocalDate date,
        @RequestParam("themeId") Long themeId
    ) {
        var availableTimeSlots = service.findAvailableTimeSlots(date, themeId);
        return ResponseEntity.ok(availableTimeSlots);
    }
}
