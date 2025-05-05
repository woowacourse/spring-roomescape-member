package roomescape.controller.timeslot;

import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.controller.timeslot.dto.AddTimeSlotRequest;
import roomescape.controller.timeslot.dto.TimeSlotResponse;
import roomescape.service.TimeSlotService;
import roomescape.service.dto.BookedTimeSlotResponse;

@Controller
public class TimeSlotController {

    private final TimeSlotService service;

    @Autowired
    public TimeSlotController(final TimeSlotService service) {
        this.service = service;
    }

    @GetMapping("/times")
    public ResponseEntity<List<TimeSlotResponse>> allTimeSlots() {
        var timeSlots = service.findAll();
        return ResponseEntity.ok(timeSlots);
    }

    @PostMapping("/times")
    public ResponseEntity<TimeSlotResponse> add(@RequestBody @Valid final AddTimeSlotRequest request) {
        var timeSlot = service.add(request);
        return ResponseEntity.created(URI.create("/times/" + timeSlot.id())).body(timeSlot);
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
        boolean isRemoved = service.removeById(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/availableTimes", params = {"date", "themeId"})
    public ResponseEntity<List<BookedTimeSlotResponse>> availableTimes(
            @RequestParam("date") LocalDate date,
            @RequestParam("themeId") Long themeId
    ) {
        var availableTimeSlots = service.findAvailableTimeSlots(date, themeId);
        return ResponseEntity.ok(availableTimeSlots);
    }
}
